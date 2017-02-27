package zesp03.servlet;

import zesp03.core.Database;
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

@WebServlet(value = "/block-password", name = "BlockPasswordServlet")
public class BlockPasswordServlet extends HttpServlet {
    public static final String POST_ID = "id";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String paramId = request.getParameter(POST_ID);
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
        UserRow userRow = null;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = em.find(User.class, id);
            if (u != null) {
                u.setBlocked(true);
                em.merge(u);
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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "no such user");
            return;
        }

        response.sendRedirect("/user?" + UserServlet.GET_ID + "=" + userRow.getId());
    }
}
