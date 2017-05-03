package zesp03.common.core;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
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
    private static final AtomicInteger examineInterval;
    private static final AtomicInteger databaseCleaningInterval;
    private static final AtomicInteger serverDelay;

    static {
        try {
            final Properties appProperties = new Properties();
            InputStream input = App.class.getResourceAsStream("/settings/app.properties");
            if(input != null) {
                appProperties.load(input);
                input.close();
            }
            final String configDirectory = appProperties.getProperty("zesp03.config.directory");
            if(configDirectory == null) {
                throw new IllegalStateException("property \"zesp03.config.directory\" required but not defined");
            }

            final Path launchPath = Paths.get(configDirectory, "launch.properties");
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
            reloadCustomProperties();
        } catch (IOException exc) {
            throw new IllegalStateException(exc);
        }
    }

    public static void reloadCustomProperties() {
        int ei = examineInterval.get();
        int dci = databaseCleaningInterval.get();
        int sd = serverDelay.get();
        final Charset utf8 = Charset.forName("UTF-8");
        try(BufferedReader br = Files.newBufferedReader(customPath, utf8)) {
            final Properties p = new Properties();
            p.load(br);
            String s = p.getProperty("zesp03.examine.interval");
            if(s != null) {
                ei = Integer.parseInt(s);
            }
            s = p.getProperty("zesp03.database.cleaning.interval");
            if(s != null) {
                dci = Integer.parseInt(s);
            }
            s = p.getProperty("zesp03.server.delay");
            if(s != null) {
                sd = Integer.parseInt(s);
            }
        }
        catch(IOException exc) {
            log.warn("reloading custom properties \"{}\"", customPath);
            log.warn("file error", exc);
        }
        catch(NumberFormatException exc) {
            log.warn("reloading custom properties \"{}\"", customPath);
            log.warn("number parsing error", exc);
        }
        if(ei >= 0) {
            examineInterval.set(ei);
        }
        else {
            log.warn("rejected zesp03.examine.interval={}", ei);
        }
        if(dci >= 0) {
            databaseCleaningInterval.set(dci);
        }
        else {
            log.warn("rejected zesp03.database.cleaning.interval={}", dci);
        }
        if(sd >= 0) {
            serverDelay.set(sd);
        }
        else {
            log.warn("rejected zesp03.server.delay={}", sd);
        }
    }

    public static void saveCustomProperties() {
        final Properties p = new Properties();
        p.setProperty("zesp03.examine.interval", Integer.toString(examineInterval.get()));
        p.setProperty("zesp03.database.cleaning.interval", Integer.toString(databaseCleaningInterval.get()));
        p.setProperty("zesp03.server.delay", Integer.toString(serverDelay.get()));
        final Charset utf8 = Charset.forName("UTF-8");
        try(BufferedWriter bw = Files.newBufferedWriter(customPath, utf8)) {
            p.store(bw, null);
        }
        catch(IOException exc) {
            log.error("failed to save settings", exc);
        }
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
     * @return zawsze >= 0
     */
    public static int getExamineInterval() {
        return examineInterval.get();
    }

    /**
     * @param examineInterval >= 0
     */
    public static void setExamineInterval(int examineInterval) {
        if(examineInterval < 0) {
            throw new IllegalArgumentException("examineInterval < 0");
        }
        App.examineInterval.set(examineInterval);
    }

    /**
     * @return zawsze >= 0
     */
    public static int getDatabaseCleaningInterval() {
        return databaseCleaningInterval.get();
    }

    /**
     * @param databaseCleaningInterval >= 0
     */
    public static void setDatabaseCleaningInterval(int databaseCleaningInterval) {
        if(databaseCleaningInterval < 0) {
            throw new IllegalArgumentException("databaseCleaningInterval < 0");
        }
        App.databaseCleaningInterval.set(databaseCleaningInterval);
    }

    /**
     * @return milisekundy
     */
    public static int getServerDelay() {
        return serverDelay.get();
    }

    /**
     * @param serverDelay milisekundy, >= 0
     */
    public static void setServerDelay(int serverDelay) {
        if(serverDelay < 0) {
            throw new IllegalArgumentException("serverDelay < 0");
        }
        App.serverDelay.set(serverDelay);
    }
}
