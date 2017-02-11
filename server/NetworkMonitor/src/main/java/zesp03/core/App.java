package zesp03.core;

import zesp03.data.DeviceStatus;
import zesp03.data.SurveyInfo;
import zesp03.entity.Controller;
import zesp03.entity.CurrentSurvey;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;
import zesp03.util.Unicode;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bardzo ważna klasa w naszym projekcie.
 * Wykonuje kluczowe operacje takie jak badanie sieci, pobieranie wyników, zarządzanie kontrolerami.
 * Wszystkie metody tej klasy są statyczne i thread-safe.
 * A przynajmniej wkrótce takie będą ;)
 */
public class App {
    private static final int USER_NAME_MAX_CHARS = 255;
    private static final int CONTROLLER_NAME_MAX_CHARS = 85;
    private static final int DEVICE_NAME_MAX_CHARS = 85;
    private static final SNMPHandler snmp;

    static {
        try {
            snmp = new FakeSNMP();
        }
        catch(IOException exc) {
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
        if( name.length() > CONTROLLER_NAME_MAX_CHARS )return false;
        return Unicode.noSurrogates(name);
    }

    public static boolean isCompatibleDeviceName(String name) {
        if( name.length() > DEVICE_NAME_MAX_CHARS )return false;
        return Unicode.noSurrogates(name);
    }

    public static String getCompatibleDeviceName(String original) {
        int len = original.length();
        if(DEVICE_NAME_MAX_CHARS < len)len = DEVICE_NAME_MAX_CHARS;
        final StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
            final char c = original.charAt(i);
            if( Character.isSurrogate(c) )sb.append('?');
            else sb.append(c);
        }
        return sb.toString().intern();
    }

    public static List<DeviceStatus> checkDevices() {
        List<DeviceStatus> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT c, d, ds, cs FROM CurrentSurvey cs " +
                    "INNER JOIN cs.survey ds " +
                    "INNER JOIN ds.device d " +
                    "INNER JOIN d.controller c", Object[].class)
                    .getResultList()
                    .stream()
                    .map(arr -> new DeviceStatus(
                            (Controller) arr[0],
                            (Device) arr[1],
                            (DeviceSurvey) arr[2]))
                    .collect(Collectors.toList());

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
        return list;
    }

    public static void examineNetwork() {
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
            examineOne(c.getId(), c.getIpv4());
        }
    }

    protected static void examineOne(long controllerId, String ipv4) {
        HashMap<String, SurveyInfo> surveyed;
        try {
            surveyed = filterDevices( snmp.queryDevices(ipv4) );
        }
        catch(SNMPException exc) {
            log("examineOne", exc);//?
            return;
        }
        if (surveyed.size() < 1) return;
        surveyed.forEach( (name, ds) -> ds.setId(-1) );

        Instant now = Instant.now();
        long longTS = now.getEpochSecond();
        if(longTS <= 0 || longTS > Integer.MAX_VALUE)
            throw new IllegalStateException("timestamp overflow");
        int timestamp = (int)longTS;

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
                    Device d = new Device();
                    d.setName(name);
                    d.setController(controller);
                    d.setIsKnown(false);
                    em.persist(d);
                    CurrentSurvey cs = new CurrentSurvey();
                    cs.setDevice(d);
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
                s.setIsEnabled(info.isEnabled());
                s.setClientsSum(info.getClientsSum());
                s.setDevice(d);
                em.persist(s);
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
        } catch (javax.persistence.PersistenceException exc) {
            log("examineOne", exc);//?
            if (tran != null && tran.isActive()) tran.rollback();
        } catch (RuntimeException exc) {
            log("examineOne", exc);//?
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * Transformuje listę urządzeń w mapę gdzie kluczem jest nazwa urządzenia.
     * Nazwy urządzeń w mapie będą przekształcone do kompatybilnej postaci za pomocą getCompatibleDeviceName.
     * W przypadku wielokrotnego wystąpienia nazwy w liscie, w mapie zostanie wpisany pierwszy element listy z daną nazwą.
     * Id każdego urządzenia w mapie będzie mieć wartość domyślną czyli -1.
     * @param list lista informacji o urządzeniach zwrócona przez SNMPHandler.
     *             Lista nie będzie modyfikowana przez tą metodę.
     * @return mapa utworzona na podstawie przefiltrowanej listy urządzeń
     */
    protected static HashMap<String, SurveyInfo> filterDevices(List<SurveyInfo> list) {
        final HashMap<String, SurveyInfo> result = new HashMap<>();
        for (SurveyInfo ds : list) {
            String name = ds.getName();
            if( ! isCompatibleDeviceName(name) ) {
                String old = name;
                name = getCompatibleDeviceName(name);
                log("filterDevices", "Device name \"" + old + "\" is not compatible.",
                        "Converted to \"" + name + "\".");
            }
            ds.setName(name);
            if( result.containsKey(name) ) {
                log("filterDevices", "Device name \"" + name + "\" occurs more than once.",
                        ds.toString() + " will be rejected.");
            }
            else {
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

    //TODO zamień na coś lepszego
    protected static void log(String method, String message, String... extra) {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("uuuu LLL dd, HH:mm:ss");
        String dt = LocalDateTime.now().format(form);
        System.err.println(dt + " App log (invoked by " + method + "):");
        System.err.println(message);
        for(String e : extra) {
            System.err.println(e);
        }
        System.err.println();
    }

    protected static void log(String method, Throwable exception, String... extra) {
        log(method, exception.toString(), extra);
    }
}
