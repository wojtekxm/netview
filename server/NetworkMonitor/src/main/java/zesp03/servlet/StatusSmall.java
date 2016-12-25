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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        ArrayList<CheckInfo> attrList;
        try {
            attrList = App.checkDevices();
        }
        catch(SQLException exc) {
            throw new IllegalStateException(exc);
        }
        req.setAttribute(ATTR_LIST, attrList);
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        req.getRequestDispatcher("WEB-INF/view/StatusSmall.jsp").include(req, resp);
    }
}
