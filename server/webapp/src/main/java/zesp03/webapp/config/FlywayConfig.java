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
