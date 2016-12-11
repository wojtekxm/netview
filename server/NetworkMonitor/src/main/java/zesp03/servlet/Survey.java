package zesp03.servlet;

import zesp03.core.Database;
import zesp03.core.Management;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Survey extends HttpServlet {
    public static final String PARAM_ACTION = "action";
    public static final String PARAM_ACTION_UPDATE = "update";
    // mapowany do typu Double
    public static final String ATTR_TIME = "zesp03.servlet.Survey.ATTR_TIME";
    // mapowany do typu Integer
    public static final String ATTR_ROWS = "zesp03.servlet.Survey.ATTR_ROWS";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Double attrTime = null;
        Integer attrRows = null;

        try {
            String action = request.getParameter(PARAM_ACTION);
            if( action.equals(PARAM_ACTION_UPDATE) ) {
                final Management m = new Management();
                long t0 = System.nanoTime();
                m.examineAll();
                long t1 = System.nanoTime();
                attrTime = (t1 - t0) * 0.000000001;
                try(Connection con = Database.connect();
                    Statement st = con.createStatement();
                    ResultSet rset = st.executeQuery("SELECT COUNT(*) FROM device_survey") ) {
                    rset.next();
                    attrRows = rset.getInt(1);
                }
            }
        }
        catch(SQLException exc) {
            throw new ServletException(exc);
        }

        request.setAttribute(ATTR_TIME, attrTime);
        request.setAttribute(ATTR_ROWS, attrRows);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        request.getRequestDispatcher("/WEB-INF/view/survey.jsp").include(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(ATTR_TIME, null);
        request.setAttribute(ATTR_ROWS, null);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        request.getRequestDispatcher("/WEB-INF/view/survey.jsp").include(request, response);
    }
}
