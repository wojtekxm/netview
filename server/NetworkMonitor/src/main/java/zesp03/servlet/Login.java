package zesp03.servlet;

import zesp03.core.App;
import zesp03.core.Database;
import zesp03.core.Secret;
import zesp03.data.UserData;
import zesp03.entity.User;
import zesp03.filter.AuthenticationFilter;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class Login extends HttpServlet {
    public static final String POST_USERNAME = "u";
    public static final String POST_PASSWORD = "p";
    public static final String GET_ERROR = "error";
    // mapuje do Boolean, opcjonalny
    public static final String ATTR_FAILED = "zesp03.servlet.Login.ATTR_FAILED";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter(POST_USERNAME);
        String password = request.getParameter(POST_PASSWORD);
        if (username != null && password != null) {
            final String hash = App.passwordToHash(password);
            UserData userData = null;

            EntityManager em = null;
            EntityTransaction tran = null;
            try {
                em = Database.createEntityManager();
                tran = em.getTransaction();
                tran.begin();
                List<User> list = em.createQuery("SELECT u FROM User u WHERE u.name = :n", User.class)
                        .setParameter("n", username)
                        .getResultList();
                if (!list.isEmpty()) {
                    User user = list.get(0);
                    if (user.getSecret() != null) {
                        Secret secret = Secret.readData(user.getSecret());
                        if (secret.check(hash.toCharArray()))
                            userData = new UserData(user);
                    }
                }
                tran.commit();
            } catch (RuntimeException exc) {
                if (tran != null && tran.isActive()) tran.rollback();
                throw exc;
            } finally {
                if (em != null) em.close();
            }

            if (userData != null) {
                Cookie cu = new Cookie(AuthenticationFilter.COOKIE_USERID, Long.toString(userData.getId()));
                cu.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(cu);
                Cookie cp = new Cookie(AuthenticationFilter.COOKIE_PASSTOKEN, hash);
                cu.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(cp);
                response.sendRedirect("/index.jsp");//? home page
                return;
            } else {
                request.setAttribute(ATTR_FAILED, Boolean.TRUE);
                request.getRequestDispatcher("/WEB-INF/view/Login.jsp").include(request, response);
                return;
            }
        }
        request.getRequestDispatcher("/WEB-INF/view/Login.jsp").include(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/Login.jsp").include(request, response);
    }
}
