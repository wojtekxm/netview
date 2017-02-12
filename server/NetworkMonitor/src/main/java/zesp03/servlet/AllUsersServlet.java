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
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/all-users", name = "AllUsersServlet")
public class AllUsersServlet extends HttpServlet {
    // mapuje do ArrayList<UserRow>
    public static final String ATTR_USERS = "zesp03.servlet.AllUsersServlet.ATTR_ALL_USERS";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final ArrayList<UserRow> allUsers = new ArrayList<>();

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List<User> list = em.createQuery("SELECT u FROM User u", User.class)
                    .getResultList();
            for (User u : list) {
                allUsers.add(new UserRow(u));
            }

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        request.setAttribute(ATTR_USERS, allUsers);
        request.getRequestDispatcher("/WEB-INF/view/AllUsers.jsp").include(request, response);
    }
}
