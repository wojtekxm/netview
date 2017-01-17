package zesp03.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Database implements ServletContextListener {
    private static EntityManagerFactory emf;

    public static synchronized EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    @Override
    public void contextInitialized(ServletContextEvent e) {
        emf = Persistence.createEntityManagerFactory("CRM");
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
        emf.close();
    }
}