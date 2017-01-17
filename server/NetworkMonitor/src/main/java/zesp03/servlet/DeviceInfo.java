package zesp03.servlet;


import zesp03.core.App;
import zesp03.data.DeviceStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        request.getRequestDispatcher("WEB-INF/view/Logged.jsp").include(request, response);
    }
}



