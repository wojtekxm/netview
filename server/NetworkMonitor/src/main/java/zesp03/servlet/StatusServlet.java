package zesp03.servlet;


import zesp03.core.App;
import zesp03.data.DeviceStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/status", name = "StatusServlet")
public class StatusServlet extends HttpServlet {

    public static final String allDevicesString = "zesp03.servlet.StatusServlet.allDevices";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<DeviceStatus> list = App.checkDevs();

        request.setAttribute(allDevicesString, list);
        request.getRequestDispatcher("WEB-INF/view/Logged.jsp").include(request, response);
    }
}



