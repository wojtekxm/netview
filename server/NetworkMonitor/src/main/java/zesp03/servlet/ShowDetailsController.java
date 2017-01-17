package zesp03.servlet;

import zesp03.core.Database;
import zesp03.entity.Controller;

import javax.naming.ldap.Control;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Berent on 2017-01-16.
 */
public class ShowDetailsController extends HttpServlet {

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
        List< Controller > controllers = ( ArrayList< Controller > )em.createQuery("SELECT c FROM Controller c").getResultList();
        Controller controller = controllers.stream().filter( (c) -> c.getId() == id).findFirst().get();

        tran.commit();
        em.close();

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");

        request.setAttribute("controller", controller );
        request.getRequestDispatcher("/WEB-INF/view/ShowDetailsController.jsp").include( request, response );
    }
}