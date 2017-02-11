package zesp03.servlet;

import zesp03.core.Database;
import zesp03.data.ControllerData;
import zesp03.entity.Controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/all-controllers", name = "AllControllersServlet")
public class AllControllersServlet extends HttpServlet {
    // mapuje do ArrayList<ControllerData>
    public static final String ATTR_LIST = "zesp03.servlet.AllControllersServlet.ATTR_LIST";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final ArrayList<ControllerData> list = new ArrayList<>();

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List<Controller> controllers = em.createQuery("SELECT c FROM Controller c", Controller.class).getResultList();
            for (Controller c : controllers) {
                list.add(new ControllerData(c));
            }

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        request.setAttribute(ATTR_LIST, list);
        request.getRequestDispatcher("/WEB-INF/view/AllControllers.jsp").include(request, response);
    }
}