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
import java.util.List;
import java.util.Random;

/**
 * Created by Kacper on 2016-12-15.
 */
public class DeviceInfo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");

//        String green="greenDiode";
//        String red="redDiode";
//        List<String> devices = new ArrayList<>();
//        List<Integer> count = new ArrayList<>();
//        String diodes[] = {"greenDiode", "redDiode"};
//        for (int i = 0; i < 400; i++) {
//            int idx = new Random().nextInt(diodes.length);
//            int c = new Random().nextInt(50);
//            String randDiode = (diodes[idx]);
////            System.out.println(randDiode);
//            devices.add(randDiode);
//            if(randDiode.equals(diodes[0])){
//                count.add(c);
//                System.out.println(c);
//            }else if(randDiode.equals(diodes[1])){
//                count.add(0);
//                System.out.println("0");
//            }
//        }
//        System.out.println(devices.size());

        List<String> diode = new ArrayList<>();
        List<Integer> count = new ArrayList<>();
        ArrayList<CheckInfo> allDevices = null;
        try {
            allDevices = App.checkDevices();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(CheckInfo dev : allDevices){

            int number = dev.survey().getClientsSum();
            count.add(number);

            if(dev.survey().isEnabled()==true && dev.survey().getClientsSum()==0) {
                diode.add("redDiode");
            }else if(dev.survey().isEnabled()==true && dev.survey().getClientsSum()!=0){
                diode.add("greenDiode");
            }else if(dev.survey().isEnabled()==false){
                diode.add("greyDiode");
            }

            dev.device().getId();
            dev.device().getControllerId();
            dev.device().getDescription();
        }
        req.setAttribute("devicesList", diode);
        req.setAttribute("countList", count);
        req.getRequestDispatcher("logged.jsp").forward(req,resp);
    }

//        private static final Random RANDOM = new Random();
//        public int getNextInt() {
//            return RANDOM.nextInt();
//        }

}



