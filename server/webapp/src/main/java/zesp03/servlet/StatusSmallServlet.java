package zesp03.servlet;

import zesp03.config.DataService;
import zesp03.data.DeviceData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/status-small", name = "StatusSmallServlet")
public class StatusSmallServlet extends HttpServlet {
    // mapuje do List<DeviceData>
    public static final String ATTR_LIST = "zesp03.servlet.StatusSmallServlet.ATTR_LIST";
    // mapuje do Double
    public static final String ATTR_TIME = "zesp03.servlet.StatusSmallServlet.ATTR_TIME";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<DeviceData> list;

        long t0 = System.nanoTime();
        list = new DataService().checkDevices();
        double time = (System.nanoTime() - t0) * 0.000000001;

        request.setAttribute(ATTR_TIME, time);
        request.setAttribute(ATTR_LIST, list);
        request.getRequestDispatcher("WEB-INF/view/StatusSmall.jsp").include(request, response);
    }
}
