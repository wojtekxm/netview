package zesp03.common.core;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final Path settingsPath;
    private static boolean flywayClean;
    private static boolean flywayMigrate;
    private static String flywayUser;
    private static String flywayPassword;
    private static String mysqlUrl;
    private static String mysqlUser;
    private static String mysqlPassword;
    private static boolean rootResetEnabled;
    private static String rootResetName;
    private static String rootResetPassword;
    private static int examineInterval;

    static {
        try {
            final Properties properties = new Properties();
            InputStream input = App.class.getResourceAsStream("/settings/app.properties");
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
            String settingsLocation = properties.getProperty("zesp03.settings.location");
            if(settingsLocation == null) {
                throw new IllegalStateException("property \"zesp03.settings.location\" required but not defined");
            }
            settingsPath = Paths.get(settingsLocation);
            if( Files.exists(settingsPath) ) {
                Charset utf8 = Charset.forName("UTF-8");
                try(BufferedReader br = Files.newBufferedReader(settingsPath, utf8)) {
                    properties.load(br);
                }
            }
            flywayClean = properties.getProperty("zesp03.flyway.clean").equals("1");
            flywayMigrate = properties.getProperty("zesp03.flyway.migrate").equals("1");
            flywayUser = properties.getProperty("zesp03.flyway.user");
            flywayPassword = properties.getProperty("zesp03.flyway.password");
            mysqlUrl = properties.getProperty("zesp03.mysql.url");
            mysqlUser = properties.getProperty("zesp03.mysql.user");
            mysqlPassword = properties.getProperty("zesp03.mysql.password");
            rootResetEnabled = properties.getProperty("zesp03.root.reset.enabled").equals("1");
            rootResetName = properties.getProperty("zesp03.root.reset.name");
            rootResetPassword = properties.getProperty("zesp03.root.reset.password");
            examineInterval = Integer.parseInt( properties.getProperty("zesp03.examine.interval") );
            if(flywayUser == null) {
                throw new IllegalStateException("property \"zesp03.flyway.user\" required but not set");
            }
            if(flywayPassword == null) {
                throw new IllegalStateException("property \"zesp03.flyway.password\" required but not set");
            }
            if(mysqlUrl == null) {
                throw new IllegalStateException("property \"zesp03.mysql.user\" required but not set");
            }
            if(mysqlUser == null) {
                throw new IllegalStateException("property \"zesp03.mysql.password\" required but not set");
            }
            if(mysqlPassword == null) {
                throw new IllegalStateException("property \"zesp03.root.reset.enabled\" required but not set");
            }
            if(rootResetName == null) {
                throw new IllegalStateException("property \"zesp03.root.reset.name\" required but not set");
            }
            if(rootResetPassword == null) {
                throw new IllegalStateException("property \"zesp03.root.reset.password\" required but not set");
            }
        } catch (IOException exc) {
            throw new IllegalStateException(exc);
        }
    }

    public static synchronized void saveSettings() {
        Properties p = new Properties();
        p.setProperty("zesp03.flyway.clean", flywayClean ? "1" : "0");
        p.setProperty("zesp03.flyway.migrate", flywayMigrate ? "1" : "0");
        p.setProperty("zesp03.flyway.user", flywayUser);
        p.setProperty("zesp03.flyway.password", flywayPassword);
        p.setProperty("zesp03.mysql.url", mysqlUrl);
        p.setProperty("zesp03.mysql.user", mysqlUser);
        p.setProperty("zesp03.mysql.password", mysqlPassword);
        p.setProperty("zesp03.root.reset.enabled", rootResetEnabled ? "1" : "0");
        p.setProperty("zesp03.root.reset.name", rootResetName);
        p.setProperty("zesp03.root.reset.password", rootResetPassword);
        p.setProperty("zesp03.examine.interval", Integer.toString(examineInterval));
        Charset utf8 = Charset.forName("UTF-8");
        try(BufferedWriter bw = Files.newBufferedWriter(settingsPath, utf8)) {
            p.store(bw, null);
        }
        catch(IOException exc) {
            log.error("Failed to save settings", exc);
        }
    }

    public static synchronized boolean isFlywayClean() {
        return flywayClean;
    }

    public static synchronized void setFlywayClean(boolean flywayClean) {
        App.flywayClean = flywayClean;
    }

    public static synchronized boolean isFlywayMigrate() {
        return flywayMigrate;
    }

    public static synchronized void setFlywayMigrate(boolean flywayMigrate) {
        App.flywayMigrate = flywayMigrate;
    }

    public static synchronized String getFlywayUser() {
        return flywayUser;
    }

    public static synchronized void setFlywayUser(String flywayUser) {
        App.flywayUser = flywayUser;
    }

    public static synchronized String getFlywayPassword() {
        return flywayPassword;
    }

    public static synchronized void setFlywayPassword(String flywayPassword) {
        App.flywayPassword = flywayPassword;
    }

    public static synchronized String getMysqlUrl() {
        return mysqlUrl;
    }

    public static synchronized void setMysqlUrl(String mysqlUrl) {
        App.mysqlUrl = mysqlUrl;
    }

    public static synchronized String getMysqlUser() {
        return mysqlUser;
    }

    public static synchronized void setMysqlUser(String mysqlUser) {
        App.mysqlUser = mysqlUser;
    }

    public static synchronized String getMysqlPassword() {
        return mysqlPassword;
    }

    public static synchronized void setMysqlPassword(String mysqlPassword) {
        App.mysqlPassword = mysqlPassword;
    }

    public static synchronized boolean isRootResetEnabled() {
        return rootResetEnabled;
    }

    public static synchronized void setRootResetEnabled(boolean rootResetEnabled) {
        App.rootResetEnabled = rootResetEnabled;
    }

    public static synchronized String getRootResetName() {
        return rootResetName;
    }

    public static void setRootResetName(String rootResetName) {
        App.rootResetName = rootResetName;
    }

    public static synchronized String getRootResetPassword() {
        return rootResetPassword;
    }

    public static synchronized void setRootResetPassword(String rootResetPassword) {
        App.rootResetPassword = rootResetPassword;
    }

    public static int getExamineInterval() {
        return examineInterval;
    }

    public static void setExamineInterval(int examineInterval) {
        App.examineInterval = examineInterval;
    }

    public static synchronized void runFlyway() {
        if(flywayClean || flywayMigrate) {
            Flyway f = new Flyway();
            f.setLocations("classpath:flyway");
            f.setDataSource(mysqlUrl, flywayUser, flywayPassword);
            f.setEncoding("UTF-8");
            if(flywayClean)f.clean();
            if(flywayMigrate)f.migrate();
        }
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
