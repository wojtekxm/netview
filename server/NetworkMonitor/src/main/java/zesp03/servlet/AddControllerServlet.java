package zesp03.servlet;

import zesp03.core.Database;
import zesp03.data.row.ControllerRow;
import zesp03.entity.Controller;
import zesp03.util.IPv4;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/add-controller", name = "AddControllerServlet")
public class AddControllerServlet extends HttpServlet {
    // wymagany, typ string
    public static final String POST_NAME = "name";
    // wymagany, typ string w formacie IPv4
    public static final String POST_IP = "ip";
    // opcjonalny, typ string
    public static final String POST_DESCRIPTION = "description";
    //TODO dodaj community string

    // mapuje do ControllerRow, nigdy null, tylko dla AddController_Post.jsp
    public static final String ATTR_CONTROLLERDATA = "zesp03.servlet.AddControllerServlet.ATTR_CONTROLLERDATA";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String paramName = request.getParameter(POST_NAME);
        if(paramName == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "name required");
            return;
        }
        final String paramIP = request.getParameter(POST_IP);
        if(paramIP == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "IP required");
            return;
        }
        if (!IPv4.isValid(paramIP)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid IP");
            return;
        }
        // opis jest opcjonalny
        String paramDesc = request.getParameter("description");
        if (paramDesc.isEmpty()) paramDesc = null;

        ControllerRow controllerRow;
        //TODO co jeśli taki kontroler już istnieje?
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller controller = new Controller();
            controller.setName(paramName);
            controller.setIpv4(paramIP);
            controller.setDescription(paramDesc);
            em.persist(controller);
            controllerRow = new ControllerRow(controller);

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        request.setAttribute(ATTR_CONTROLLERDATA, controllerRow);
        request.getRequestDispatcher("/WEB-INF/view/AddController_Post.jsp").include(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/AddController_Get.jsp").include(request, response);
    }
}