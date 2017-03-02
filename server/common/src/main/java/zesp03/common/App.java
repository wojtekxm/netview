package zesp03.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.entity.*;
import zesp03.util.Unicode;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bardzo ważna klasa w naszym projekcie.
 * Wykonuje kluczowe operacje takie jak badanie sieci, pobieranie wyników, zarządzanie kontrolerami.
 * Wszystkie metody tej klasy są statyczne i thread-safe.
 * A przynajmniej wkrótce takie będą ;)
 */
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final int USER_NAME_MAX_CHARS = 255;
    private static final int CONTROLLER_NAME_MAX_CHARS = 85;
    private static final int DEVICE_NAME_MAX_CHARS = 85;
    private static final SNMPHandler snmp;

    static {
        try {
            snmp = new FakeSNMP();
        } catch (IOException exc) {
            throw new IllegalStateException(exc);
        }
    }

    /**
     * @param name nazwa użytkownika do sprawdzenia
     * @return czy nazwa użytkownika składa się z poprawnych znaków (niezależnie od tego czy jest już zajęta)
     */
    public static boolean isValidUserName(String name) {
        if (name == null || name.isEmpty()) return false;
        return Unicode.onlyAlphaNum(name) && name.length() <= USER_NAME_MAX_CHARS;
    }

    public static boolean isValidControllerName(String name) {
        if (name == null || name.isEmpty()) return false;
        if (name.length() > CONTROLLER_NAME_MAX_CHARS) return false;
        return Unicode.noSurrogates(name);
    }

    public static boolean isCompatibleDeviceName(String name) {
        if (name.length() > DEVICE_NAME_MAX_CHARS) return false;
        return Unicode.noSurrogates(name);
    }

    public static String getCompatibleDeviceName(String original) {
        int len = original.length();
        if (DEVICE_NAME_MAX_CHARS < len) len = DEVICE_NAME_MAX_CHARS;
        final StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            final char c = original.charAt(i);
            if (Character.isSurrogate(c)) sb.append('?');
            else sb.append(c);
        }
        return sb.toString().intern();
    }

    public static void examineNetwork() {
        final Instant t0 = Instant.now();
        log.info("network survey begins");
        List<Controller> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT c FROM Controller c", Controller.class).getResultList();

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        for (Controller c : list) {
            try {
                examineOne(c.getId(), c.getIpv4());
            } catch (RuntimeException exc) {
                log.error("Failed to examine controller", exc);
            }
        }

        final Instant t1 = Instant.now();
        double dur = Duration.between(t0, t1).toMillis() * 0.001;
        log.info("network survey took {} seconds to examine {} controllers", dur, list.size());
    }

    protected static void examineOne(long controllerId, String ipv4) {
        HashMap<String, SurveyInfo> surveyed;
        try {
            surveyed = filterDevices(snmp.queryDevices(ipv4));
        } catch (SNMPException exc) {
            log.warn("Failed to query devices", exc);
            return;
        }
        if (surveyed.size() < 1) return;
        surveyed.forEach((name, ds) -> ds.setId(-1));

        Instant now = Instant.now();
        long longTS = now.getEpochSecond();
        if (longTS <= 0 || longTS > Integer.MAX_VALUE)
            throw new IllegalStateException("timestamp overflow");
        int timestamp = (int) longTS;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller controller = em.find(Controller.class, controllerId);
            if (controller == null) {
                tran.commit();
                return;
            }

            List<Device> devices = em.createQuery("SELECT d FROM Device d", Device.class).getResultList();
            final HashMap<String, Device> existing = new HashMap<>();
            for (Device d : devices) {
                existing.put(d.getName(), d);
            }

            int x = 0;
            for (Map.Entry<String, SurveyInfo> e : surveyed.entrySet()) {
                final String name = e.getKey();
                if (!existing.containsKey(name)) {
                    final Device d = new Device();
                    d.setName(name);
                    d.setController(controller);
                    d.setKnown(false);
                    em.persist(d);
                    final CurrentSurvey cs = new CurrentSurvey();
                    d.addCurrentSurvey(cs);
                    em.persist(cs);
                    existing.put(name, d);
                    if (++x == 50) {
                        em.flush();
                        em.clear();
                        x = 0;
                    }
                }
            }

            x = 0;
            for (Map.Entry<String, SurveyInfo> e : surveyed.entrySet()) {
                final String name = e.getKey();
                final SurveyInfo info = e.getValue();
                final Device d = existing.get(name);
                final DeviceSurvey s = new DeviceSurvey();
                s.setTimestamp(timestamp);
                s.setEnabled(info.isEnabled());
                s.setClientsSum(info.getClientsSum());
                s.setDevice(d);
                em.persist(s);
                buildRangeSurvey(s, em);
                CurrentSurvey cs = s.getDevice().getCurrentSurvey();
                if (cs == null) {
                    cs = new CurrentSurvey();
                    cs.setDevice(s.getDevice());
                    cs.setSurvey(s);
                    em.persist(cs);
                } else {
                    cs.setSurvey(s);
                    em.merge(cs);
                }
                if (++x == 50) {
                    em.flush();
                    em.clear();
                    x = 0;
                }
            }

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * @param deviceId identyfikator urządzenia
     * @throws IllegalArgumentException device with given id does not exist
     */
    public static void rebuildRangeSurveys(long deviceId) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Device device = em.find(Device.class, deviceId);
            if(device == null)
                throw new IllegalArgumentException("device with given id does not exist");

            em.createQuery("DELETE FROM RangeSurvey rs WHERE rs.device.id = :deviceid")
                    .setParameter("deviceid", device.getId())
                    .executeUpdate();
            List<DeviceSurvey> list = em.createQuery(
                    "SELECT ds FROM DeviceSurvey ds WHERE ds.device = :device ORDER BY ds.timestamp ASC",
                    DeviceSurvey.class)
                    .setParameter("device", device)
                    .getResultList();
            for( DeviceSurvey survey : list ) {
                log.debug("survey id = {}, time = {}, clients = {}", survey.getId(), survey.getTimestamp(), survey.getClientsSum());//!!!
                buildRangeSurvey(survey, em);
            }

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    private static void buildRangeSurvey(DeviceSurvey survey, EntityManager em) {
        long time = survey.getTimestamp();
        List<RangeSurvey> list = em.createQuery(
                "SELECT rs FROM RangeSurvey rs WHERE rs.surveyRange = 1 AND rs.timeStart = :t AND rs.device = :d",
                RangeSurvey.class)
                .setParameter("t", time)
                .setParameter("d", survey.getDevice())
                .getResultList();
        RangeSurvey fresh = null;
        if(list.isEmpty()) {
            RangeSurvey rs = new RangeSurvey();
            rs.setDevice(survey.getDevice());
            rs.setTimeStart(time);
            rs.setTimeEnd(time);
            rs.setTimeRange(0L);
            rs.setTotalSum((long) survey.getClientsSum());
            rs.setMin(survey.getClientsSum());
            rs.setMax(survey.getClientsSum());
            rs.setSurveyRange(1L);
            em.persist(rs);
            fresh = rs;
        }
        while(fresh != null) {
            List<RangeSurvey> allBefore = em.createQuery(
                    "SELECT rs FROM RangeSurvey rs WHERE rs.device = :device AND rs.surveyRange = :range " +
                            "AND rs.timeEnd < :time ORDER BY rs.timeEnd DESC ",
                    RangeSurvey.class)
                    .setParameter("device", fresh.getDevice())
                    .setParameter("range", fresh.getSurveyRange())
                    .setParameter("time", fresh.getTimeStart())
                    .setMaxResults(1)
                    .getResultList();
            log.debug("allBefore.size = {}", allBefore.size());//!!!
            if(allBefore.isEmpty())break;
            RangeSurvey justBefore = allBefore.get(0);
            List<RangeSurvey> allCovering = em.createQuery(
                    "SELECT rs FROM RangeSurvey rs WHERE rs.device = :device AND " +
                            "rs.surveyRange = :range AND rs.timeEnd = :time",
                    RangeSurvey.class)
                    .setParameter("device", justBefore.getDevice())
                    .setParameter("range", justBefore.getSurveyRange() * 2)
                    .setParameter("time", justBefore.getTimeEnd())
                    .getResultList();
            log.debug("allCovering.size = {}", allCovering.size());//!!!
            if(!allCovering.isEmpty())break;
            RangeSurvey x = new RangeSurvey();
            x.setDevice(fresh.getDevice());
            x.setTimeStart(justBefore.getTimeStart());
            x.setTimeEnd(fresh.getTimeEnd());
            x.setTimeRange( x.getTimeEnd() - x.getTimeStart() );
            x.setTotalSum( justBefore.getTotalSum() + fresh.getTotalSum() );
            x.setMin( Integer.min(justBefore.getMin(), fresh.getMin()) );
            x.setMax( Integer.max(justBefore.getMax(), fresh.getMax()) );
            x.setSurveyRange( justBefore.getSurveyRange() + fresh.getSurveyRange() );
            em.persist(x);
            log.debug("start={} end={} trange={} tsum={} min={} max={} srange={}",
                    x.getTimeStart(), x.getTimeEnd(), x.getTimeRange(),
                    x.getTotalSum(), x.getMin(), x.getMax(), x.getSurveyRange());//!!!
            fresh = x;
        }
    }

    /**
     * Transformuje listę urządzeń w mapę gdzie kluczem jest nazwa urządzenia.
     * Nazwy urządzeń w mapie będą przekształcone do kompatybilnej postaci za pomocą getCompatibleDeviceName.
     * W przypadku wielokrotnego wystąpienia nazwy w liscie, w mapie zostanie wpisany pierwszy element listy z daną nazwą.
     * Id każdego urządzenia w mapie będzie mieć wartość domyślną czyli -1.
     *
     * @param list lista informacji o urządzeniach zwrócona przez SNMPHandler.
     *             Lista nie będzie modyfikowana przez tą metodę.
     * @return mapa utworzona na podstawie przefiltrowanej listy urządzeń
     */
    protected static HashMap<String, SurveyInfo> filterDevices(List<SurveyInfo> list) {
        final HashMap<String, SurveyInfo> result = new HashMap<>();
        for (SurveyInfo ds : list) {
            String name = ds.getName();
            if (!isCompatibleDeviceName(name)) {
                String old = name;
                name = getCompatibleDeviceName(name);
                log.info("Device name \"{}\" is not compatible, converted to {}", old, name);
            }
            ds.setName(name);
            if (result.containsKey(name)) {
                log.info("Device name \"{}\" occurs more than once, {} will be rejected", name, ds.toString());
            } else {
                result.put(name, ds);
            }
        }
        return result;
    }

    /**
     * @param password hasło dla którego ma być wyznaczony hash
     * @return hash funkcji SHA-256 dla podanego hasła, zaprezentowany w formacie Base64URL.
     */
    public static String passwordToHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] binaryPassword = password.getBytes("UTF-8");
            byte[] binaryHash = md.digest(binaryPassword);
            return Base64.getUrlEncoder().encodeToString(binaryHash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exc) {
            throw new IllegalStateException(exc);
        }
    }

    /**
     * Generuje losowy token w formacie Base64URL, reprezentujący ciąg bajtów o długości randomBytes.
     *
     * @param randomBytes liczba losowych bajtów do wygenerowania
     * @return napis Base64URL reprezentujący losowo wygenerowany token.
     */
    public static String generateToken(int randomBytes) {
        byte[] bin = new byte[randomBytes];
        new SecureRandom().nextBytes(bin);
        return Base64.getUrlEncoder().encodeToString(bin);
    }
}
