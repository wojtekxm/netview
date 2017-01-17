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

/**
 * Created by Berent on 2017-01-17.
 */
public class RemoveController extends HttpServlet {

    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    }
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        removeController(request);

        response.sendRedirect( "ShowControllers" );
    }

    //------------------------------------------------------------------------------------------------------------------

    private void removeController( HttpServletRequest request ) {

        final EntityManager em = Database.createEntityManager();
        final EntityTransaction tran = em.getTransaction();

        tran.begin();

        int id = Integer.parseInt( request.getParameter( "id" ) );

        Controller c = em.find( Controller.class, id );
        em.remove( c );

        tran.commit();
        em.close();
    }
}
