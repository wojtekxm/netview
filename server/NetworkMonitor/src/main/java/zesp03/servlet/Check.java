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

    private void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        try(PrintWriter w = resp.getWriter() ) {
            String x = req.getParameter("name");
            String y = req.getParameter("ipv4");
            String z = req.getParameter("description");
            w.println("<!DOCTYPE html>");
            w.println("<html lang=\"pl\"><head>");
            w.println("<meta charset=\"utf-8\">");
            w.println("<title>check</title>");
            w.println("</head><body>");

            if(x == null)w.println("brak parametru!<br><br>");
            else w.println("Dodano kontroler o nazwie: " + x + "<br><br>");

            if(x == null)w.println("brak parametru!<br><br>");
            else w.println("Adres IPv4 kontrolera: " + y + "<br><br>");

            if(x == null)w.println("brak parametru!<br><br>");
            else w.println("Komentarz: " + z + "<br><br>");

            w.println("</body></html>");



        }
    }
}
