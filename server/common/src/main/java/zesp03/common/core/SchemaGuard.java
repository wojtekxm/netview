/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.core;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Zapewnia aktualny schemat bazy danych.
 */
public class SchemaGuard {
    private static final Logger log = LoggerFactory.getLogger(SchemaGuard.class);

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
            if(c) {
                try {
                    List<String> list = readSQL("insert.sql");
                    executeSQL(list);
                }
                catch(IOException | SQLException exc) {
                    throw new IllegalStateException(exc);
                }
            }
        }
    }

    protected static List<String> readSQL(String resourceName) throws IOException {
        InputStream input = SchemaGuard.class.getResourceAsStream(resourceName);
        if(input == null) {
            throw new IllegalArgumentException("resource not found");
        }
        try(InputStreamReader isr = new InputStreamReader(input, "UTF-8");
            BufferedReader br = new BufferedReader(isr)) {
            List<String> list = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            br.lines().forEachOrdered( line -> {
                sb.append(line);
                sb.append(' ');
                if(line.trim().endsWith(";")) {
                    list.add(sb.toString().trim().intern());
                    sb.delete(0, Integer.MAX_VALUE);
                }
            } );
            return list;
        }
    }

    protected static void executeSQL(List<String> sqlList) throws SQLException {
        Driver d = DriverManager.getDriver(Config.getMysqlUrl());
        Properties p = new Properties();
        p.setProperty("user", Config.getMysqlUser());
        p.setProperty("password", Config.getMysqlPassword());
        try(Connection con = d.connect(Config.getMysqlUrl(), p)) {
            con.setAutoCommit(false);
            for(String sql : sqlList) {
                try (Statement st = con.createStatement()) {
                    log.info("executing SQL:\n{}", sql);
                    st.executeUpdate(sql);
                }
            }
            con.commit();
        }
    }
}
