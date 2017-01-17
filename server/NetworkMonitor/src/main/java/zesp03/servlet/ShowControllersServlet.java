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
import java.util.Arrays;
import java.util.List;


public class ShowControllersServlet  extends HttpServlet {
    //==================================================================================================================
    // SERVLET METHODS
    //==================================================================================================================
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Powinno usuwać wszystkie kontrolery jednym zapytaniem do bazy, zamiast robienia zapytania per id kontrolera
        for(final String controllerId : Arrays.asList(request.getParameterValues("ids-to-delete"))) {
            int id = Integer.parseInt(controllerId);
            final EntityManager em = Database.createEntityManager();
            final EntityTransaction tran = em.getTransaction();
            tran.begin();

            Controller c = em.find(Controller.class, id);
            em.remove(c);

            tran.commit();
            em.close();
        }
        // TODO: Wyświetl informacje o kontrolerach które udało się usunąć, których nie itp, itd.
        handle(request, response); // Po prostu wyrenderuj pozostałe urządzenia
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    protected void handle( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        List<Controller> list = null;

        final EntityManager em = Database.createEntityManager();
        final EntityTransaction tran = em.getTransaction();
        tran.begin();

        list = em.createQuery("SELECT c FROM Controller c", Controller.class).getResultList();

        tran.commit();
        em.close();

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        request.setAttribute("controllers", list);
        request.getRequestDispatcher("/WEB-INF/view/ShowControllersJSP.jsp").include( request, response );
    }
}