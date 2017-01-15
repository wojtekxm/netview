package zesp03.core;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class Database implements ServletContextListener {
    private static final DataSource ds = null;
    private static EntityManagerFactory emf;

    /*static {
        try {
            Context ctx0 = new InitialContext();
            Context ctx1 = (Context) ctx0.lookup("java:comp/env");
            ds = (DataSource) ctx1.lookup("jdbc/MySQLDB");
        }
        catch(NamingException exc) {
            throw new IllegalStateException(exc);
        }
    }*/

    /**
     * Łączy się z MariaDB jako użytkownik dbuser.
     * @return połączenie do bazy "pz" jako użytkownik dbuser
     * @throws SQLException nie udało się pobrać połączenia do bazy danych
     */
    public static synchronized Connection connect() throws SQLException {
        return ds.getConnection();
    }

    public static synchronized EntityManagerFactory getEMF() {
        return emf;
    }

    @Override
    public synchronized void contextInitialized(ServletContextEvent e) {
        emf = Persistence.createEntityManagerFactory("CRM");
        System.out.println("initialized");
    }

    @Override
    public synchronized void contextDestroyed(ServletContextEvent e) {
        emf = null;
        System.out.println("destroyed");
    }
}