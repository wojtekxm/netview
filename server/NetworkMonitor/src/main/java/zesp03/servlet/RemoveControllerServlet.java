package zesp03.servlet;

import zesp03.core.Database;
import zesp03.entity.Controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/remove-controller", name = "RemoveControllerServlet")
public class RemoveControllerServlet extends HttpServlet {
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        removeController(request);
        response.sendRedirect("/all-controllers");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        response.sendRedirect("/all-controllers");
    }

    //------------------------------------------------------------------------------------------------------------------

    private void removeController( HttpServletRequest request ) {

        final EntityManager em = Database.createEntityManager();
        final EntityTransaction tran = em.getTransaction();

        tran.begin();

        Long id = Long.parseLong(request.getParameter("id"));

        Controller c = em.find( Controller.class, id );
        em.remove( c );

        tran.commit();
        em.close();
    }
}
