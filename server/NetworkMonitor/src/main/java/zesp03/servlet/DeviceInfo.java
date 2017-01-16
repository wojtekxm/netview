package zesp03.servlet;


import zesp03.core.App;
import zesp03.data.CheckInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Kacper on 2016-12-15.
 */
public class DeviceInfo extends HttpServlet {

    public static final String allDevicesString = "zesp03.servlet.DeviceInfo.allDevices";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        ArrayList<CheckInfo> allDevices;
        try {
            allDevices = App.checkDevices();
        }
        catch(SQLException exc) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "database error");
            return;
        }

        request.setAttribute(allDevicesString, allDevices);
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");

        HttpSession session = request.getSession(true);

        String uname=request.getParameter("uname");
        String pass=request.getParameter("pass");

        if(null != (session.getAttribute("error"))){
            session.removeAttribute("error");
        }

        if(uname != null) {
            if (uname.equals("user") && pass.equals("user")) {
                session = request.getSession();
                session.setAttribute("username", uname);
                request.getRequestDispatcher("WEB-INF/view/Logged.jsp").forward(request, response);
            } else {
                session.setAttribute("error", "Podano zły login lub hasło");
                request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            }
        }else{
            session.setAttribute("error", "Podano zły login lub hasło");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
        }
    }

}



