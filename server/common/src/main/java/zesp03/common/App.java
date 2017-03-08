package zesp03.common;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.entity.*;
import zesp03.util.Unicode;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

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
    private static final Properties properties = new Properties();

    static {
        try {
            snmp = new FakeSNMP();

            InputStream input = App.class.getResourceAsStream("/settings/default.properties");
            if(input != null) {
                properties.load(input);
                input.close();
            }
            input = App.class.getResourceAsStream("/settings/modified.properties");
            if(input != null) {
                properties.load(input);
                input.close();
            }
            System.getProperties()
                    .stringPropertyNames()
                    .forEach( key -> {
                        if(key.startsWith("zesp03.")) {
                            properties.setProperty(key, System.getProperties().getProperty(key));
                        }
                    } );
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

    public static boolean isValidPassword(String password) {
        return password != null && ! password.isEmpty();
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

    /**
     * @return The method returns null if the property is not found.
     */
    public static synchronized String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * @return The method returns the default value argument if the property is not found.
     */
    public static synchronized String getProperty(String key, String def) {
        return properties.getProperty(key, def);
    }

    public static void runFlyway() {
        boolean clean = getProperty("zesp03.flyway.clean", "0").equals("1");
        boolean migrate = getProperty("zesp03.flyway.migrate", "1").equals("1");
        if(clean || migrate) {
            Flyway f = new Flyway();
            f.setLocations("classpath:flyway");
            f.setDataSource(
                    getProperty("zesp03.mysql.url"),
                    getProperty("zesp03.flyway.user"),
                    getProperty("zesp03.flyway.password") );
            if(clean)f.clean();
            if(migrate)f.migrate();
        }
    }

    public static void examineAll() {
        final Instant t0 = Instant.now();
        log.info("network survey begins");
        List<Long> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT c.id FROM Controller c", Long.class)
                    .getResultList();

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        for (Long id : list) {
            try {
                examineOne(id);
            } catch (RuntimeException | SNMPException exc) {
                log.error("failed to examine controller", exc);
            }
        }

        final Instant t1 = Instant.now();
        double dur = Duration.between(t0, t1).toMillis() * 0.001;
        log.info("network survey took {} seconds to examine {} controllers", dur, list.size());
    }

    /**
     * Wykonuje badanie wskazanego kontrolera.
     * @param controllerId id kontrolera
     * @return liczba zaktualizowanych urządzeń (liczba nowych zapisanych badań)
     */
    public static int examineOne(long controllerId)
            throws SNMPException {
        String ipv4;
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller c = em.find(Controller.class, controllerId);
            if(c == null)
                throw new IllegalArgumentException("no such controller with id=" + controllerId);
            ipv4 = c.getIpv4();

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        final HashMap<String, SurveyInfo> surveyed = filterDevices(snmp.queryDevices(ipv4));
        if (surveyed.size() < 1) {
            log.info("controller returned no devices");
            return 0;
        }
        surveyed.forEach((name, ds) -> ds.setId(-1));

        Instant now = Instant.now();
        long longTS = now.getEpochSecond();
        if (longTS <= 0 || longTS > Integer.MAX_VALUE)
            throw new IllegalStateException("timestamp overflow");
        int timestamp = (int) longTS;

        em = null;
        tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller controller = em.find(Controller.class, controllerId);
            if (controller == null) {
                tran.rollback();
                throw new IllegalArgumentException("no such controller");
            }

            List<Device> devices = em.createQuery("SELECT d FROM Device d", Device.class).getResultList();
            final HashMap<String, Device> existing = new HashMap<>();
            for (Device d : devices) {
                existing.put(d.getName(), d);
            }

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
                }
            }

            final List<DeviceSurvey> ds2persist = new ArrayList<>();
            final List<CurrentSurvey> cs2persist = new ArrayList<>();
            final List<CurrentSurvey> cs2merge = new ArrayList<>();
            final List<RangeSurvey> rs2persist = new ArrayList<>();
            for (Map.Entry<String, SurveyInfo> e : surveyed.entrySet()) {
                final String name = e.getKey();
                final SurveyInfo info = e.getValue();
                final Device d = existing.get(name);
                final DeviceSurvey s = new DeviceSurvey();
                s.setTimestamp(timestamp);
                s.setEnabled(info.isEnabled());
                s.setClientsSum(info.getClientsSum());
                s.setDevice(d);
                CurrentSurvey cs = d.getCurrentSurvey();
                ds2persist.add(s);
                if (cs == null) {
                    s.setCumulative(0L);
                    cs = new CurrentSurvey();
                    cs.setDevice(d);
                    cs.setSurvey(s);
                    cs2persist.add(cs);
                } else {
                    DeviceSurvey before = cs.getSurvey();
                    if(before == null)s.setCumulative(0L);
                    else s.setCumulative( before.getCumulative() + before.getClientsSum() *
                            ( s.getTimestamp() - before.getTimestamp() ) );
                    cs.setSurvey(s);
                    cs2merge.add(cs);
                }
            }
            for(DeviceSurvey ds : ds2persist) {
                em.persist(ds);
            }
            for(CurrentSurvey cs : cs2persist) {
                em.persist(cs);
            }
            for(CurrentSurvey cs : cs2merge) {
                em.merge(cs);
            }
            for(DeviceSurvey ds : ds2persist) {
                buildRangeSurvey(ds, rs2persist, em);
            }
            for(RangeSurvey rs : rs2persist) {
                em.persist(rs);
            }

            tran.commit();
            log.info("survey of controller (id={}, ip={}) has finished successfully with {} updated devices",
                    controllerId, ipv4, ds2persist.size());
            return ds2persist.size();
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

            em.createQuery("DELETE FROM RangeSurvey rs WHERE rs.device = :device")
                    .setParameter("device", device)
                    .executeUpdate();
            List<DeviceSurvey> list = em.createQuery(
                    "SELECT ds FROM DeviceSurvey ds WHERE ds.device = :device ORDER BY ds.timestamp ASC",
                    DeviceSurvey.class)
                    .setParameter("device", device)
                    .getResultList();
            for( DeviceSurvey survey : list ) {
                final List<RangeSurvey> rs2persist = new ArrayList<>();
                buildRangeSurvey(survey, rs2persist, em);
                for(RangeSurvey rs : rs2persist) {
                    em.persist(rs);
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

    private static void buildRangeSurvey(DeviceSurvey survey, List<RangeSurvey> rs2persist, EntityManager em) {
        long time = survey.getTimestamp();
        List<RangeSurvey> list = em.createQuery(
                "SELECT rs FROM RangeSurvey rs WHERE rs.device = :device AND rs.surveyRange = 1 AND rs.timeStart = :time",
                RangeSurvey.class)
                .setParameter("time", time)
                .setParameter("device", survey.getDevice())
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
            rs2persist.add(rs);
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
            rs2persist.add(x);
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
