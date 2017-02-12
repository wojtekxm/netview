package zesp03.servlet;

import zesp03.core.App;
import zesp03.data.DeviceStatusData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/status-small", name = "StatusSmallServlet")
public class StatusSmallServlet extends HttpServlet {
    // mapuje do List<DeviceStatusData>
    public static final String ATTR_STATES = "zesp03.servlet.StatusSmallServlet.ATTR_STATES";
    // mapuje do Double
    public static final String ATTR_TIME = "zesp03.servlet.StatusSmallServlet.ATTR_TIME";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<DeviceStatusData> states;

        long t0 = System.nanoTime();
        states = App.checkDevices();
        double time = (System.nanoTime() - t0) * 0.000000001;

        request.setAttribute(ATTR_TIME, time);
        request.setAttribute(ATTR_STATES, states);
        request.getRequestDispatcher("WEB-INF/view/StatusSmall.jsp").include(request, response);
    }
}