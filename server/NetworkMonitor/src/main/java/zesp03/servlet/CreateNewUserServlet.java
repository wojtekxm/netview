package zesp03.servlet;

import zesp03.core.App;
import zesp03.core.Database;
import zesp03.core.Secret;
import zesp03.entity.Token;
import zesp03.entity.TokenAction;
import zesp03.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/create-new-user", name = "CreateNewUserServlet")
public class CreateNewUserServlet extends HttpServlet {
    // opcjonalny, oczekiwany typ boolean (1/0)
    public static final String POST_IS_ADMIN = "is-admin";
    // mapuje do Long
    public static final String ATTR_TOKEN_ID = "zesp03.servlet.CreateNewUserServlet.ATTR_TOKEN_ID";
    // mapuje do String
    public static final String ATTR_TOKEN_VALUE = "zesp03.servlet.CreateNewUserServlet.ATTR_TOKEN_VALUE";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean isAdmin = false;
        String paramIsAdmin = request.getParameter(POST_IS_ADMIN);
        if (paramIsAdmin != null && paramIsAdmin.equals("1"))
            isAdmin = true;

        String tokenValue = App.generateToken(32);
        Secret secret = Secret.create(tokenValue.toCharArray(), 1);
        Long tokenId;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = new User();
            u.setName(null);
            u.setSecret(null);
            u.setIsAdmin(isAdmin);
            em.persist(u);

            Token t = new Token();
            t.setSecret(secret.getData());
            t.setAction(TokenAction.ACTIVATE_ACCOUNT);
            t.setUser(u);
            em.persist(t);
            tokenId = t.getId();

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        request.setAttribute(ATTR_TOKEN_ID, tokenId);
        request.setAttribute(ATTR_TOKEN_VALUE, tokenValue);
        request.getRequestDispatcher("/WEB-INF/view/CreateNewUser.jsp").include(request, response);
    }
}
