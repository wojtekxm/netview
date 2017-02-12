package zesp03.servlet;

import zesp03.core.Database;
import zesp03.data.row.ControllerRow;
import zesp03.entity.Controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/controller", name = "ControllerServlet")
public class ControllerServlet extends HttpServlet {
    // wymagane, typ long
    public static final String GET_ID = "id";

    // mapuje do ControllerRow
    public static final String ATTR_CONTROLLERDATA = "zesp03.servlet.ControllerServlet.ATTR_CONTROLLERDATA";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String paramId = request.getParameter(GET_ID);
        if (paramId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "id required");
            return;
        }
        long id;
        try {
            id = Long.parseLong(paramId);
        } catch (NumberFormatException exc) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid id");
            return;
        }
        ControllerRow controllerRow;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller c = em.find(Controller.class, id);
            controllerRow = new ControllerRow(c);

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        request.setAttribute(ATTR_CONTROLLERDATA, controllerRow);
        request.getRequestDispatcher("/WEB-INF/view/Controller.jsp").include(request, response);
    }
}