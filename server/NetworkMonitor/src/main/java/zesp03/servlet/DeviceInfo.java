package zesp03.servlet;


import zesp03.core.App;
import zesp03.data.DeviceStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class DeviceInfo extends HttpServlet {

    public static final String allDevicesString = "zesp03.servlet.DeviceInfo.allDevices";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        List<DeviceStatus> list = App.checkDevs();

        request.setAttribute(allDevicesString, list);
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");

        String uname=request.getParameter("uname");
        String pass=request.getParameter("pass");

        HttpSession session = request.getSession(true);
        if(null != (session.getAttribute("error"))){
            session.removeAttribute("error");
        }

        if(uname != null && pass != null) {
            if (uname.equals("user") && pass.equals("user")) {
                session = request.getSession();
                session.setAttribute("username", uname);
                request.getRequestDispatcher("WEB-INF/view/Logged.jsp").forward(request, response);
            } else {
                session.setAttribute("error", "Podano zły login lub hasło");
                request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            }
        }else{
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
        }
    }

}



