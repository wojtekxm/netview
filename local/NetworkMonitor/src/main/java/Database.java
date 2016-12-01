import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/pz?user=dbuser&password=1234");
    }
}
