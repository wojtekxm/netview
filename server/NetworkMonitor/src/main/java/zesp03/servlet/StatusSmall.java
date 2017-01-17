package zesp03.servlet;

import zesp03.core.App;
import zesp03.data.DeviceStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class StatusSmall extends HttpServlet {
    // mapuje do List<DeviceStatus>
    public static final String ATTR_STATES = "zesp03.servlet.StatusSmall.ATR_STATES";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        List<DeviceStatus> states;

        states = App.checkDevs();

        request.setAttribute(ATTR_STATES, states);
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        request.getRequestDispatcher("WEB-INF/view/StatusSmall.jsp").include(request, response);
    }
}
