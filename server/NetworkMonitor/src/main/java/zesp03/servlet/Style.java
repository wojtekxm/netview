package zesp03.servlet;

import zesp03.core.App;
import zesp03.data.DeviceStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static zesp03.servlet.DeviceInfo.allDevicesString;

public class Style extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

        List<DeviceStatus> list;
        list = App.checkDevs();
        request.setAttribute(allDevicesString, list);
        request.getRequestDispatcher("deviceinfo").forward(request, response);
    }
}
