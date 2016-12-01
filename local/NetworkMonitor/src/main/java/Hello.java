import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Hello {
    public static void main(String[] args) throws SQLException {
        try(Connection c = DriverManager.getConnection("jdbc:mariadb://localhost:3306/pz?user=dbuser&password=1234") ) {
            System.out.println("Pobrałem połączenie");
        }
    }
}
