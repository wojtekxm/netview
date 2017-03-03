package zesp03.config;

import zesp03.common.App;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class FlywayConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        App.runFlyway();
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
    }
}
