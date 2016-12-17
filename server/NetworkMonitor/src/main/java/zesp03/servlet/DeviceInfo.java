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
public class DeviceInfo extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");

//        String green="greenDiode";
//        String red="redDiode";
        List<String> devices = new ArrayList<>();
        List<Integer> count = new ArrayList<>();
        String diodes[] = {"greenDiode", "redDiode"};
        for (int i = 0; i < 400; i++) {
            int idx = new Random().nextInt(diodes.length);
            int c = new Random().nextInt(50);
            String randDiode = (diodes[idx]);
//            System.out.println(randDiode);
            devices.add(randDiode);
            if(randDiode.equals(diodes[0])){
                count.add(c);
                System.out.println(c);
            }else if(randDiode.equals(diodes[1])){
                count.add(0);
                System.out.println("0");
            }
        }
        System.out.println(devices.size());
        req.setAttribute("devicesList", devices);
        req.setAttribute("countList", count);
        req.getRequestDispatcher("logged.jsp").forward(req,resp);
    }

        private static final Random RANDOM = new Random();

        public int getNextInt() {
            return RANDOM.nextInt();
        }

}



