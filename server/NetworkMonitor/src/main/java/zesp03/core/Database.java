package zesp03.core;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private static final DataSource ds;

    static {
        try {
            Context ctx0 = new InitialContext();
            Context ctx1 = (Context) ctx0.lookup("java:comp/env");
            ds = (DataSource) ctx1.lookup("jdbc/MySQLDB");
        }
        catch(NamingException exc) {
            throw new IllegalStateException(exc);
        }
    }

    /**
     * Łączy się z MariaDB jako użytkownik dbuser.
     * @return połączenie do bazy "pz" jako użytkownik dbuser
     * @throws SQLException nie udało się pobrać połączenia do bazy danych
     */
    public static synchronized Connection connect() throws SQLException {
        return ds.getConnection();
    }
}
