package zesp03.servlet;

import zesp03.core.Database;
import zesp03.entity.Controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
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
        final EntityManager em = Database.getEMF().createEntityManager();
        EntityTransaction t = em.getTransaction();
        t.begin();
        Controller c = new Controller();
        c.setId( (int) System.nanoTime() );
        c.setName("tak-zimno" + System.nanoTime() );
        c.setDescription("Stark");
        c.setIpv4("9.9.9.9");
        em.persist(c);
        t.commit();
        em.close();
        try( PrintWriter w = response.getWriter() ) {
            w.println("zima nadeszła");
        }
    }
}
