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

@WebServlet(value = "/controller", name = "ControllerServlet")
public class ControllerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        handle(request, response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        handle(request, response);
    }

    //------------------------------------------------------------------------------------------------------------------

    protected void handle( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        final EntityManager em = Database.createEntityManager();
        final EntityTransaction tran = em.getTransaction();

        tran.begin();

        int id = Integer.valueOf( request.getParameter( "id" ) );
        List<Controller> controllers = em.createQuery("SELECT c FROM Controller c", Controller.class).getResultList();
        Controller controller = controllers.stream().filter( (c) -> c.getId() == id).findFirst().get();

        tran.commit();
        em.close();

        request.setAttribute("controller", controller );
        request.getRequestDispatcher("/WEB-INF/view/Controller.jsp").include(request, response);
    }
}