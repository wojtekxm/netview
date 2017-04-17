package zesp03.common.core;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final Properties properties = new Properties();

    static {
        try {
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
            f.setEncoding("UTF-8");
            if(clean)f.clean();
            if(migrate)f.migrate();
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
