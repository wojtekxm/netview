package zesp03.servlet;

import zesp03.common.Database;
import zesp03.data.row.UserRow;
import zesp03.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/user", name = "UserServlet")
public class UserServlet extends HttpServlet {
    public static final String GET_ID = "id";
    // mapuje do UserRow, nigdy null
    public static final String ATTR_USERROW = "zesp03.servlet.UserServlet.ATTR_USERROW";

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
        } catch (NumberFormatException ignore) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid id");
            return;
        }
        UserRow userRow = null;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();
            User u = em.find(User.class, id);
            if (u != null) {
                userRow = new UserRow(u);
            }
            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        if (userRow == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "no such user");
            return;
        }
        request.setAttribute(ATTR_USERROW, userRow);
        request.getRequestDispatcher("/WEB-INF/view/User.jsp").include(request, response);
    }
}
