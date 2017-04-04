package zesp03.common.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static EntityManagerFactory emf;

    public static synchronized EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static synchronized void init() {
        Map<String, String> map = new HashMap<>();
        map.put("javax.persistence.jdbc.password", App.getProperty("zesp03.mysql.password"));
        map.put("javax.persistence.jdbc.user", App.getProperty("zesp03.mysql.user"));
        map.put("javax.persistence.jdbc.url", App.getProperty("zesp03.mysql.url"));
        emf = Persistence.createEntityManagerFactory("CRM", map);
    }

    public static synchronized void destroy() {
        emf.close();
        emf = null;
    }
}