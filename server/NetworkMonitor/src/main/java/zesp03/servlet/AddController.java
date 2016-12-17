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

    Connection conn = null;
    Statement stmt;
    ResultSet rset = null;
    PreparedStatement pstmt = null;
    String idg;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        String x = req.getParameter("name");
        String y = req.getParameter("ipv4");
        String z = req.getParameter("description");
        try( Connection con = Database.connect();
             Statement st = con.createStatement();
             ResultSet rset = st.executeQuery("INSERT INTO CONTROLLER VALUES(x, y, z)") ) {
             rset.next();

        }
        catch(SQLException exc) {
            throw new ServletException(exc);
        }

        try(PrintWriter w = resp.getWriter() ) {
            w.println("Dodano do bazy kontroler o nazwie " + x + " adresie PIv4 " + y);
        }


        }


    }

