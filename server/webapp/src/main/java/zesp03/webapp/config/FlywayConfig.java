/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.config;

import zesp03.common.core.SchemaGuard;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class FlywayConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        SchemaGuard.runFlyway();
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
    }
}
