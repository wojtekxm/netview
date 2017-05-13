package zesp03.common.core;

import org.flywaydb.core.Flyway;

/**
 * Zapewnia aktualny schemat bazy danych.
 */
public class SchemaGuard {
    /**
     * Powinna być wykonana tylko raz, przy uruchamianiu aplikacji.
     */
    public static synchronized void runFlyway() {
        final boolean c = Config.isFlywayClean();
        final boolean m = Config.isFlywayMigrate();
        if(c || m) {
            Flyway f = new Flyway();
            f.setLocations("classpath:zesp03.common.flyway");
            f.setDataSource(
                    Config.getMysqlUrl(),
                    Config.getFlywayUser(),
                    Config.getFlywayPassword()
            );
            f.setEncoding("UTF-8");
            if(c)f.clean();
            if(m)f.migrate();
        }
    }
}
