package zesp03.servlet;

import zesp03.core.Database;
import zesp03.entity.Device;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WinterIsComing extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain");

        int id;
        try {
            id = Integer.parseInt( request.getParameter("id") );
        }
        catch(NumberFormatException exc) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid id");
            return;
        }

        final EntityManager em = Database.createEntityManager();
        EntityTransaction t = em.getTransaction();
        t.begin();

        Device d = em.find(Device.class, id);
        if(d == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "no such device");
            return;
        }
        int s = d.getDeviceSurveys().size();
        //d.getController();

        t.commit();
        em.close();

        try( PrintWriter w = response.getWriter() ) {
            w.println("device.getDeviceSurveys().size() = " + d.getDeviceSurveys().size());
            w.println("device id = " + d.getId());
            w.println("device name = " + d.getName());
            w.println("controller id = " + d.getController().getId());
            w.println("controller name = " + d.getController().getName());
        }
    }
}
