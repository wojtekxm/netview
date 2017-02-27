package zesp03.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.function.Consumer;

public class Database {
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
            em = Database.createEntityManager();
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

    //TODO ???
    public static synchronized void init() {
        emf = Persistence.createEntityManagerFactory("CRM");
    }

    //TODO ???
    public static synchronized void destroy() {
        emf.close();
        emf = null;
    }
}