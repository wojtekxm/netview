package zesp03.servlet;

import zesp03.core.App;
import zesp03.data.CheckInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class StatusSmall extends HttpServlet {
    // mapuje do ArrayList<CheckInfo>
    public static final String ATTR_LIST = "zesp03.servlet.StatusSmall.ATTR_LIST";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        ArrayList<CheckInfo> attrList;
        try {
            attrList = App.checkDevices();
        }
        catch(SQLException exc) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "database error");
            return;
        }
        request.setAttribute(ATTR_LIST, attrList);
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        request.getRequestDispatcher("WEB-INF/view/StatusSmall.jsp").include(request, response);
    }
}
