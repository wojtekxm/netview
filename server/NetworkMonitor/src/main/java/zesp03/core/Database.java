package zesp03.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.function.Consumer;

public class Database implements ServletContextListener {
    private static EntityManagerFactory emf;

    public static synchronized EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    /**
     * Uruchamia podany kod na bazie danych, dostarczając właściwy EntityManager i standardową obsługę transakcji.
     * Jest to jedynie pomocnicza funkcja by zaoszczędzić pisania.
     * W dostarczonym kodzie lambda nie należy zamykać managera, ani pobierać transakcji.
     *
     * @param action kod do wykonania
     */
    public static void run(Consumer<EntityManager> action) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = createEntityManager();
            tran = em.getTransaction();
            tran.begin();
            action.accept(em);
            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
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