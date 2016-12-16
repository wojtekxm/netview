package zesp03.servlet;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Kacper on 2016-12-15.
 */
public class DeviceStatus extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");

        List<String> devices = new ArrayList<>();
        String diodes[] = {"greenDiode", "redDiode"};
        for (int i = 0; i < 400; i++) {
            int idx = new Random().nextInt(diodes.length);
            String randDiode = (diodes[idx]);
            System.out.println(randDiode);
            devices.add(randDiode);
        }
        System.out.println(devices.size());
        req.setAttribute("devicesList", devices);
        req.getRequestDispatcher("logged.jsp").forward(req,resp);
    }

}



