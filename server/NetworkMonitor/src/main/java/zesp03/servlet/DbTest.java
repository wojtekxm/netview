package zesp03.servlet;

import zesp03.core.Database;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by wojte on 09.12.2016.
 */
public class DbTest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");

        int devices;
        try( Connection con = Database.connect();
             Statement st = con.createStatement();
             ResultSet rset = st.executeQuery("SELECT COUNT(*) FROM device") ) {
            rset.next();
            devices = rset.getInt(1);
        }
        catch(SQLException exc) {
            throw new ServletException(exc);
        }

        try(PrintWriter w = resp.getWriter() ) {
            w.println("baza danych działa sprawnie");
            w.println("w bazie jest " + devices + " urządzeń");
        }
    }
}
