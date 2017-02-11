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
import java.util.List;

@WebServlet(value = "/all-controllers", name = "AllControllersServlet")
public class AllControllersServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        handle(request, response);
    }

    //------------------------------------------------------------------------------------------------------------------

    protected void handle( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        final EntityManager em = Database.createEntityManager();
        final EntityTransaction tran = em.getTransaction();

        tran.begin();

        List<Controller> attrList = em.createQuery("SELECT c FROM Controller c", Controller.class).getResultList();

        tran.commit();
        em.close();

        request.setAttribute("controllers", attrList);
        request.getRequestDispatcher("/WEB-INF/view/AllControllers.jsp").include(request, response);
    }
}