package zesp03.servlet;

import zesp03.core.Database;
import zesp03.entity.Controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AddController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");

        final String paramName = req.getParameter("name");
        if(paramName == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "name required");
            return;
        }
        final String paramIP = req.getParameter("ipv4");
        if(paramIP == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "IP required");
            return;
        }
        if( ! isValidIPv4(paramIP) ) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid IP");
            return;
        }
        final String paramDesc = req.getParameter("description");
        // opis jest opcjonalny

        //TODO co jeśli taki kontroler już istnieje?
        final EntityManager em = Database.createEntityManager();
        final EntityTransaction tran = em.getTransaction();
        tran.begin();
        Controller controller = new Controller();
        controller.setName(paramName);
        controller.setIpv4(paramIP);
        controller.setDescription(paramDesc);
        em.persist(controller);
        tran.commit();
        em.close();

        try(PrintWriter w = resp.getWriter() ) {
            w.println("Dodano do bazy kontroler o nazwie=" + controller.getName() +
                    " adresie IPv4=" + controller.getIpv4() +
                    " oraz dodatkowym komentarzu=" + controller.getDescription() + ".");
        }
    }

    public static boolean isValidIPv4(String ip) {
        //TODO naiwna implementacja, poprawić
        return ip.matches("[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}+");
    }
}