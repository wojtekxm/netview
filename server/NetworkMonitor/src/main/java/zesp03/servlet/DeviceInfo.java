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

//        for(CheckInfo dev : allDevices){
//
//            int number = dev.survey().getClientsSum();
//            count.add(number);
//
//            if(dev.survey().isEnabled()==true && dev.survey().getClientsSum()==0) {
//                diode.add("redDiode");
//            }else if(dev.survey().isEnabled()==true && dev.survey().getClientsSum()!=0){
//                diode.add("greenDiode");
//            }else if(dev.survey().isEnabled()==false){
//                diode.add("greyDiode");
//            }
//
//            dev.device().getId();
//            dev.device().getControllerId();
//            dev.device().getDescription();
//        }
        request.setAttribute(allDevicesString, allDevices);
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        request.getRequestDispatcher("WEB-INF/view/Logged.jsp").forward(request,response);
    }

}



