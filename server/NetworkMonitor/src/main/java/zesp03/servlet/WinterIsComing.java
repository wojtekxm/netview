package zesp03.servlet;

import zesp03.core.Database;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        final EntityManager em = Database.createEntityManager();
        EntityTransaction t = em.getTransaction();
        t.begin();

        try( PrintWriter w = response.getWriter() ) {
            w.println("Winter has come");
            w.println("And winter is gone");
        }

        t.commit();
        em.close();
    }
}
