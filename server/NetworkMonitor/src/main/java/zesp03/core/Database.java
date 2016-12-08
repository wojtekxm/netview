package zesp03.core;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    /**
     * Łączy się z MariaDB jako użytkownik dbuser.
     * @return połączenie do bazy "pz" jako użytkownik dbuser
     * @throws SQLException nie udało się pobrać połączenia do bazy danych
     */
    public static Connection connect() throws SQLException, NamingException {
        Context ctx0 = new InitialContext();
        Context ctx1 = (Context)ctx0.lookup( "java:comp/env" );
        DataSource ds = (DataSource)ctx1.lookup( "jdbc/mariadb" );
        return ds.getConnection();
    }
}
