package zesp03.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Check extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    protected void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        try(PrintWriter w = resp.getWriter() ) {
            String x = req.getParameter("x");
            w.println("<!DOCTYPE html>");
            w.println("<html lang=\"pl\"><head>");
            w.println("<meta charset=\"utf-8\">");
            w.println("<title>check</title>");
            w.println("</head><body>");

            if(x == null)w.println("brak parametru!<br><br>");
            else w.println("otrzymano parametr: " + x + "<br><br>");

            w.println("<a href=\"test.jsp\">test.jsp</a><br>");
            w.println("<a href=\"check\">check</a><br>");
            w.println("</body></html>");
        }
    }
}
