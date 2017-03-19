package zesp03.webapp.config;

import zesp03.common.core.Database;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DatabaseConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        Database.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
        Database.destroy();
    }
}