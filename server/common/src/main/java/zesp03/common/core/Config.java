package zesp03.common.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zesp03.common.exception.ValidationException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Trzyma globalną konfigurację aplikacji. Zawiera same statyczne metody, jest thread-safe.
 */
public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static final Path customPath;

    private static final boolean flywayClean;
    private static final boolean flywayMigrate;
    private static final String flywayUser;
    private static final String flywayPassword;
    private static final String mysqlUrl;
    private static final String mysqlUser;
    private static final String mysqlPassword;
    private static final boolean rootResetEnabled;
    private static final String rootResetName;
    private static final String rootResetPassword;
    private static final AtomicInteger examineInterval; // sekundy
    private static final AtomicInteger databaseCleaningInterval; // sekundy
    private static final AtomicInteger serverDelay; // milisekundy
    private static final AtomicInteger tokenPasswordExpiration; // minuty
    private static final AtomicInteger tokenActivateExpiraton; // minuty
    private static final AtomicInteger tokenAccessExpiration; // minuty
    private static final AtomicReference<String> adminMailUsername;
    private static final AtomicReference<String> adminMailPassword;
    private static final AtomicReference<String> adminMailSmtpHost;
    private static final AtomicInteger adminMailSmtpPort;
    private static final AtomicLong lastReloadTime;

    static {
        try {
            final Properties appProperties = new Properties();
            InputStream input = Config.class.getResourceAsStream("/settings/app.properties");
            if(input != null) {
                appProperties.load(input);
                input.close();
            }
            final String configDirectory1 = appProperties.getProperty("zesp03.config.directory");
            if(configDirectory1 == null) {
                throw new IllegalStateException("property \"zesp03.config.directory\" required but not defined");
            }

            Path launchPath = Paths.get(configDirectory1, "launch.properties");
            String configDirectory = configDirectory1;
            if(!Files.exists(launchPath)) {
                final String configDirectory2 = appProperties.getProperty("zesp03.config.directory2");
                if(configDirectory2 != null) {
                    launchPath = Paths.get(configDirectory2, "launch.properties");
                    configDirectory = configDirectory2;
                }
            }
            final Charset utf8 = Charset.forName("UTF-8");
            try(BufferedReader br = Files.newBufferedReader(launchPath, utf8)) {
                final Properties p = new Properties();
                p.load(br);
                flywayClean = p.getProperty("zesp03.flyway.clean").equals("1");
                flywayMigrate = p.getProperty("zesp03.flyway.migrate").equals("1");
                flywayUser = p.getProperty("zesp03.flyway.user");
                flywayPassword = p.getProperty("zesp03.flyway.password");
                mysqlUrl = p.getProperty("zesp03.mysql.url");
                mysqlUser = p.getProperty("zesp03.mysql.user");
                mysqlPassword = p.getProperty("zesp03.mysql.password");
                rootResetEnabled = p.getProperty("zesp03.root.reset.enabled").equals("1");
                rootResetName = p.getProperty("zesp03.root.reset.name");
                rootResetPassword = p.getProperty("zesp03.root.reset.password");

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
            }

            customPath = Paths.get(configDirectory, "custom.properties");
            examineInterval = new AtomicInteger(300);
            databaseCleaningInterval = new AtomicInteger(100);
            serverDelay = new AtomicInteger(0);
            tokenAccessExpiration = new AtomicInteger(15);
            tokenActivateExpiraton = new AtomicInteger(60 * 24);
            tokenPasswordExpiration = new AtomicInteger(60 * 24);
            adminMailUsername = new AtomicReference<>("");
            adminMailPassword = new AtomicReference<>("");
            adminMailSmtpHost = new AtomicReference<>("");
            adminMailSmtpPort = new AtomicInteger(465);
            lastReloadTime = new AtomicLong(-1);
            forceReloadCustomProperties();
        } catch (IOException exc) {
            throw new IllegalStateException(exc);
        }
    }

    public static void forceReloadCustomProperties() {
        final Charset utf8 = Charset.forName("UTF-8");
        try(BufferedReader br = Files.newBufferedReader(customPath, utf8)) {
            final Properties p = new Properties();
            p.load(br);
            parseNonNegative(p, "zesp03.examine.interval", examineInterval);
            parseNonNegative(p, "zesp03.database.cleaning.interval", databaseCleaningInterval);
            parseNonNegative(p, "zesp03.server.delay", serverDelay);
            parseNonNegative(p, "zesp03.token.access", tokenAccessExpiration);
            parseNonNegative(p, "zesp03.token.activate", tokenActivateExpiraton);
            parseNonNegative(p, "zesp03.token.password", tokenPasswordExpiration);
            parseNotNull(p, "zesp03.admin.mail.username", adminMailUsername);
            parseNotNull(p, "zesp03.admin.mail.password", adminMailPassword);
            parseNotNull(p, "zesp03.admin.mail.smtphost", adminMailSmtpHost);
            parseNonNegative(p, "zesp03.admin.mail.smtpport", adminMailSmtpPort);
        }
        catch(IOException exc) {
            log.warn("failed reloading custom properties \"{}\"", customPath);
        }
        lastReloadTime.set(Instant.now().toEpochMilli());
    }

    private static void parseNonNegative(Properties p, String key, AtomicInteger target) {
        String v = p.getProperty(key);
        if(v != null) {
            try {
                int x = Integer.parseInt(v);
                if (x >= 0) {
                    target.set(x);
                }
                else {
                    log.warn("rejected " + key + "={}", v);
                }
            }
            catch(NumberFormatException exc) {
                log.warn("failed to parse " + key + "={} as number", v);
            }
        }
    }

    private static void parseNotNull(Properties p, String key, AtomicReference<String> target) {
        String v = p.getProperty(key);
        if(v != null) {
            target.set(v);
        }
    }

    public static synchronized void saveCustomProperties() {
        final Properties p = new Properties();
        p.setProperty("zesp03.examine.interval", Integer.toString(examineInterval.get()));
        p.setProperty("zesp03.database.cleaning.interval", Integer.toString(databaseCleaningInterval.get()));
        p.setProperty("zesp03.server.delay", Integer.toString(serverDelay.get()));
        p.setProperty("zesp03.token.access", Integer.toString(tokenAccessExpiration.get()));
        p.setProperty("zesp03.token.activate", Integer.toString(tokenActivateExpiraton.get()));
        p.setProperty("zesp03.token.password", Integer.toString(tokenPasswordExpiration.get()));
        p.setProperty("zesp03.admin.mail.username", adminMailUsername.get());
        p.setProperty("zesp03.admin.mail.password", adminMailPassword.get());
        p.setProperty("zesp03.admin.mail.smtphost", adminMailSmtpHost.get());
        p.setProperty("zesp03.admin.mail.smtpport", Integer.toString(adminMailSmtpPort.get()));
        final Charset utf8 = Charset.forName("UTF-8");
        try(BufferedWriter bw = Files.newBufferedWriter(customPath, utf8)) {
            p.store(bw, null);
        }
        catch(IOException exc) {
            log.error("failed to save settings", exc);
        }
    }

    private static void handleCachedProperties() {
        final long now = Instant.now().toEpochMilli();
        if(now > lastReloadTime.get() + 60000) {
            forceReloadCustomProperties();
        }
    }

    public static boolean isFlywayClean() {
        return flywayClean;
    }

    public static boolean isFlywayMigrate() {
        return flywayMigrate;
    }

    public static String getFlywayUser() {
        return flywayUser;
    }

    public static String getFlywayPassword() {
        return flywayPassword;
    }

    public static String getMysqlUrl() {
        return mysqlUrl;
    }

    public static String getMysqlUser() {
        return mysqlUser;
    }

    public static String getMysqlPassword() {
        return mysqlPassword;
    }

    public static boolean isRootResetEnabled() {
        return rootResetEnabled;
    }

    public static String getRootResetName() {
        return rootResetName;
    }

    public static String getRootResetPassword() {
        return rootResetPassword;
    }

    /**
     * @return okres w sekundach wykonywania badań, liczba nieujemna
     */
    public static int getExamineInterval() {
        handleCachedProperties();
        return examineInterval.get();
    }

    /**
     * @param examineInterval okres w sekundach wykonywania badań, liczba nieujemna
     */
    public static void setExamineInterval(int examineInterval) {
        if(examineInterval < 0) {
            throw new ValidationException("examineInterval", "negative");
        }
        Config.examineInterval.set(examineInterval);
    }

    /**
     * @return okres w sekundach czyszczenia bazy, liczba nieujemna
     */
    public static int getDatabaseCleaningInterval() {
        handleCachedProperties();
        return databaseCleaningInterval.get();
    }

    /**
     * @param databaseCleaningInterval okres w sekundach czyszczenia bazy, liczba nieujemna
     */
    public static void setDatabaseCleaningInterval(int databaseCleaningInterval) {
        if(databaseCleaningInterval < 0) {
            throw new ValidationException("databaseCleaningInterval", "negative");
        }
        Config.databaseCleaningInterval.set(databaseCleaningInterval);
    }

    /**
     * @return milisekundy opóźnienia API, liczba nieujemna
     */
    public static int getServerDelay() {
        handleCachedProperties();
        return serverDelay.get();
    }

    /**
     * @param serverDelay milisekundy opóźnienia API, liczba nieujemna
     */
    public static void setServerDelay(int serverDelay) {
        if(serverDelay < 0) {
            throw new ValidationException("serverDelay", "negative");
        }
        Config.serverDelay.set(serverDelay);
    }

    /**
     * @return minuty
     */
    public static int getTokenPasswordExpiration() {
        handleCachedProperties();
        return tokenPasswordExpiration.get();
    }

    /**
     * @param tokenPasswordExpiration minuty
     */
    public static void setTokenPasswordExpiration(int tokenPasswordExpiration) {
        if(tokenPasswordExpiration < 0) {
            throw new ValidationException("tokenPasswordExpiration", "negative");
        }
        Config.tokenPasswordExpiration.set(tokenPasswordExpiration);
    }

    /**
     * @return minuty
     */
    public static int getTokenActivateExpiraton() {
        handleCachedProperties();
        return tokenActivateExpiraton.get();
    }

    /**
     * @param tokenActivateExpiraton minuty
     */
    public static void setTokenActivateExpiraton(int tokenActivateExpiraton) {
        if(tokenActivateExpiraton < 0) {
            throw new ValidationException("tokenActivateExpiraton", "negative");
        }
        Config.tokenActivateExpiraton.set(tokenActivateExpiraton);
    }

    /**
     * @return minuty
     */
    public static int getTokenAccessExpiration() {
        handleCachedProperties();
        return tokenAccessExpiration.get();
    }

    /**
     * @param tokenAccessExpiration minuty
     */
    public static void setTokenAccessExpiration(int tokenAccessExpiration) {
        if(tokenAccessExpiration < 0) {
            throw new ValidationException("tokenAccessExpiration", "negative");
        }
        Config.tokenAccessExpiration.set(tokenAccessExpiration);
    }

    /**
     * @return adres e-mail administratora na serwerze pocztowym, nigdy null
     */
    public static String getAdminMailUsername() {
        handleCachedProperties();
        return adminMailUsername.get();
    }

    /**
     * @param adminMailUsername adres e-mail administratora na serwerze pocztowym, nigdy null
     */
    public static void setAdminMailUsername(String adminMailUsername) {
        if(adminMailUsername == null) {
            throw new ValidationException("adminMailUsername", "null");
        }
        Config.adminMailUsername.set(adminMailUsername);
    }

    /**
     * @return hasło do konta e-mail administratora na serwerze pocztowym, nigdy null
     */
    public static String getAdminMailPassword() {
        handleCachedProperties();
        return adminMailPassword.get();
    }

    /**
     * @param adminMailPassword hasło do konta e-mail administratora na serwerze pocztowym, nigdy null
     */
    public static void setAdminMailPassword(String adminMailPassword) {
        if(adminMailPassword == null) {
            throw new ValidationException("adminMailPassword", "null");
        }
        Config.adminMailPassword.set(adminMailPassword);
    }

    /**
     * @return adres serwera pocztowego SMTP, nigdy null
     */
    public static String getAdminMailSmtpHost() {
        handleCachedProperties();
        return adminMailSmtpHost.get();
    }

    /**
     * @param adminMailSmtpHost adres serwera pocztowego SMTP, nigdy null
     */
    public static void setAdminMailSmtpHost(String adminMailSmtpHost) {
        if(adminMailSmtpHost == null) {
            throw new ValidationException("adminMailSmtpHost", "null");
        }
        Config.adminMailSmtpHost.set(adminMailSmtpHost);
    }

    /**
     * @return numer portu SMTP na serwerze poczty
     */
    public static int getAdminMailSmtpPort() {
        handleCachedProperties();
        return adminMailSmtpPort.get();
    }

    /**
     * @param adminMailSmtpPort numer portu SMTP na serwerze poczty
     */
    public static void setAdminMailSmtpPort(int adminMailSmtpPort) {
        if(adminMailSmtpPort < 1) {
            throw new ValidationException("adminMailSmtpPort", "less than 1");
        }
        Config.adminMailSmtpPort.set(adminMailSmtpPort);
    }
}
