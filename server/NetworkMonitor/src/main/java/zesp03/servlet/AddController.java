package zesp03.servlet;

import zesp03.core.Database;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class AddController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");

        final String paramName = req.getParameter("name");
        if(paramName == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "name required");
            return;
        }
        final String paramIP = req.getParameter("ipv4");
        if(paramIP == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "IP required");
            return;
        }
        if( ! isValidIPv4(paramIP) ) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid IP");
            return;
        }
        final String paramDesc = req.getParameter("description");
        // opis jest opcjonalny

        String sql = "INSERT INTO controller (name, ipv4, description) VALUES (?, ?, ?)";
        try(Connection con = Database.connect();
            PreparedStatement pstmt = con.prepareStatement(sql) ) {
            pstmt.setString(1, paramName);
            pstmt.setString(2, paramIP);
            pstmt.setString(3, paramDesc);
            pstmt.executeUpdate();
        }
        catch(SQLException exc) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "database error" + exc.toString());
            return;
        }
        try(PrintWriter w = resp.getWriter() ) {
            w.println("Dodano do bazy kontroler o nazwie=" + paramName + " adresie IPv4=" + paramIP + " oraz dodatkowym komentarzu=" + paramDesc + ".");
        }
    }

    public static boolean isValidIPv4(String ip) {
        // nie do końca poprawne
        return ip.matches("[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}+");
    }
}