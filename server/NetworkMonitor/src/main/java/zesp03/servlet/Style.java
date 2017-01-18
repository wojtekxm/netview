package zesp03.servlet;

import zesp03.core.App;
import zesp03.data.CheckInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static zesp03.servlet.DeviceInfo.allDevicesString;

/**
 * Created by Kacper on 2017-01-18.
 */
public class Style extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");

        HttpSession session = request.getSession(true);


        String style = (String)session.getAttribute("style");
        String logo = (String)session.getAttribute("logo");
        System.out.println(style);
        System.out.println(logo);

        if(style.equals("loggedStyleBlack") && logo.equals("logoo")) {
            session.setAttribute("style", "loggedStyleWhite");
            session.setAttribute("logo", "logooWhite");
        }else if(style.equals("loggedStyleWhite") && logo.equals("logooWhite")){
            session.setAttribute("style", "loggedStyleBlack");
            session.setAttribute("logo", "logoo");
        }

        ArrayList<CheckInfo> allDevices;
        try {
            allDevices = App.checkDevices();
        }
        catch(SQLException exc) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "database error");
            return;
        }
        request.setAttribute(allDevicesString, allDevices);

        request.getRequestDispatcher("deviceinfo").forward(request, response);
    }
}
