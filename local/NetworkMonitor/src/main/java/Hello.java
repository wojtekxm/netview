import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Hello {
    public static void main(String[] args) throws SQLException {
        try(Connection c = Database.connect() ) {
            System.out.println("Baza danych jest dobrze skonfigurowana");
        }
    }
}
