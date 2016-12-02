package zesp03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    /**
     * Łączy się z MariaDB jako użytkownik dbuser.
     * @return połączenie do bazy "pz" jako użytkownik dbuser
     * @throws SQLException nie udało się pobrać połączenia do bazy danych
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/pz?user=dbuser&password=1234");
    }
}
