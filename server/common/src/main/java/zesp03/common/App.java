package zesp03.common;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.data.DeviceNow;
import zesp03.data.ExamineResult;
import zesp03.data.SurveyInfo;
import zesp03.entity.Controller;
import zesp03.entity.Device;
import zesp03.entity.DeviceSurvey;
import zesp03.service.SurveyService;
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
import java.util.stream.Collectors;

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

    public static ExamineResult examineAll() {
        final Instant start = Instant.now();
        log.info("network survey begins");
        List<Long> list;
        final ExamineResult r = new ExamineResult();
        r.setUpdatedDevices(0);

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
                r.setUpdatedDevices( r.getUpdatedDevices() + examineOne(id).getUpdatedDevices() );
            } catch (RuntimeException exc) {
                log.error("failed to examine controller", exc);
            }
        }

        r.setSeconds(Duration.between(start, Instant.now()).toNanos() * 0.000000001);
        log.info("network survey examined {} controllers with {} updated devices, in {} seconds",
                list.size(), r.getUpdatedDevices(), r.getSeconds() );
        return r;
    }

    /**
     * Wykonuje badanie wskazanego kontrolera.
     * @param controllerId id kontrolera
     * @return liczba zaktualizowanych urządzeń (liczba nowych zapisanych badań)
     */
    public static ExamineResult examineOne(long controllerId) {
        final Instant start = Instant.now();
        final int timestamp = (int) start.getEpochSecond();
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

        final HashSet<String> deviceNames = new HashSet<>();
        final HashMap<String, SurveyInfo> name2info = new HashMap<>();
        for(SurveyInfo info : snmp.queryDevices(ipv4)) {
            deviceNames.add(info.getName());
            name2info.put(info.getName(), info);
        }
        if(name2info.isEmpty()) {
            log.info("survey of controller (id={}, ip={}) returned no devices", controllerId, ipv4);
            final ExamineResult r = new ExamineResult();
            r.setSeconds(Duration.between(start, Instant.now()).toNanos() * 0.000000001);
            r.setUpdatedDevices(0);
            return r;
        }

        em = null;
        tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller controller = em.find(Controller.class, controllerId);
            if (controller == null) {
                throw new IllegalArgumentException("no such controller with id=" + controllerId);
            }

            final HashMap<String, Device> name2device = makeDevices(deviceNames, controller, em);
            em.flush();
            final HashMap<Long, DeviceNow> devid2now = new HashMap<>();
            new SurveyService().checkSome(
                    name2device
                            .entrySet()
                            .stream()
                            .map( e -> e.getValue().getId() )
                            .collect(Collectors.toSet()),
                    em
            ).forEach( di -> {
                devid2now.put(di.getId(), di);
            } );
            final List<DeviceSurvey> ds2persist = new ArrayList<>();
            for(String name : deviceNames) {
                final SurveyInfo info = name2info.get(name);
                final Device device = name2device.get(name);
                final DeviceSurvey sur = new DeviceSurvey();
                sur.setTimestamp(timestamp);
                sur.setEnabled(info.isEnabled());
                sur.setClientsSum(info.getClientsSum());
                sur.setDevice(device);
                final DeviceNow before = devid2now.get(device.getId());
                if(before == null || before.getSurvey() == null) {
                    sur.setCumulative(0L);
                }
                else {
                    DeviceSurvey z = before.getSurvey();
                    sur.setCumulative( z.getCumulative() + z.getClientsSum() *
                            ( sur.getTimestamp() - z.getTimestamp() ) );
                }
                ds2persist.add(sur);
            }
            for(DeviceSurvey ds : ds2persist) {
                em.persist(ds);
            }

            tran.commit();
            final ExamineResult r = new ExamineResult();
            r.setUpdatedDevices(ds2persist.size());
            r.setSeconds(Duration.between(start, Instant.now()).toNanos() * 0.000000001);
            log.info("survey of controller (id={}, ip={}) has finished successfully with {} updated devices, in {} seconds",
                    controllerId, ipv4, r.getUpdatedDevices(), r.getSeconds());
            return r;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * Dla każdej nazwy z <code>list</code> wstawia do bazy nowe urządzenie jeśli takie nie istnieje.
     * @return mapa [nazwa urządzenia => urządzenie z bazy]
     */
    protected static HashMap<String, Device> makeDevices(Set<String> deviceNames, Controller controller, EntityManager em) {
        if(deviceNames.isEmpty())return new HashMap<>();
        final HashMap<String, Device> existing = new HashMap<>();
        for(String name : deviceNames) {
            existing.put(name, null);
        }
        em.createQuery("SELECT d FROM Device d WHERE d.name IN (:names)", Device.class)
                .setParameter("names", deviceNames)
                .getResultList()
                .forEach( device -> existing.put(device.getName(), device) );
        final HashMap<String, Device> result = new HashMap<>();
        existing.forEach( (name, device) -> {
            if(device == null) {
                Device d = new Device();
                d.setName(name);
                d.setController(controller);
                d.setKnown(false);
                d.setDescription(null);
                em.persist(d);
                result.put(name, d);
            }
            else result.put(name, device);
        } );
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
