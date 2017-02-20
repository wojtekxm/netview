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

@WebServlet(value = "/activate-account", name = "ActivateAccountServlet")
public class ActivateAccountServlet extends HttpServlet {
    // wymagany, typ long
    public static final String GET_TOKEN_ID = "tid";
    // wymagany, typ string w formacie base64url
    public static final String GET_TOKEN_VALUE = "tv";

    // wymagany, typ long
    public static final String POST_TOKEN_ID = "tid";
    // wymagany, typ string w formacie base64url
    public static final String POST_TOKEN_VALUE = "tv";
    // wymagany, typ string
    public static final String POST_USERNAME = "n";
    // wymagany, typ string
    public static final String POST_PASSWORD1 = "p1";
    // wymagany, typ string
    public static final String POST_PASSWORD2 = "p2";

    // mapuje do Long, tylko dla ActivateAccount_Get.jsp
    public static final String ATTR_TOKEN_ID = "zesp03.servlet.ActivateAccountServlet.ATTR_TOKEN_ID";
    // mapuje do String, tylko dla ActivateAccount_Get.jsp
    public static final String ATTR_TOKEN_VALUE = "zesp03.servlet.ActivateAccountServlet.ATTR_TOKEN_VALUE";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String paramId = request.getParameter(GET_TOKEN_ID);
        if (paramId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "token id required");
            return;
        }
        long tokenId;
        try {
            tokenId = Long.parseLong(paramId);
        } catch (NumberFormatException exc) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid token id");
            return;
        }
        String paramValue = request.getParameter(GET_TOKEN_VALUE);
        if (paramValue == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "token value required");
            return;
        }
        boolean success = false;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Token token = em.find(Token.class, tokenId);
            if (token != null &&
                    token.getAction() == TokenAction.ACTIVATE_ACCOUNT &&
                    !token.getUser().isActivated() &&
                    Secret.check(token.getSecret(), paramValue) ) {
                success = true;
            }

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        if (!success) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        request.setAttribute(ATTR_TOKEN_ID, tokenId);
        request.setAttribute(ATTR_TOKEN_VALUE, paramValue);
        request.getRequestDispatcher("/WEB-INF/view/ActivateAccount_Get.jsp").include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String paramTokenId = request.getParameter(POST_TOKEN_ID);
        if (paramTokenId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "token id required");
            return;
        }
        long tokenId;
        try {
            tokenId = Long.parseLong(paramTokenId);
        } catch (NumberFormatException exc) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid token id");
            return;
        }
        String paramTokenValue = request.getParameter(POST_TOKEN_VALUE);
        if (paramTokenValue == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "token value required");
            return;
        }
        String paramUserName = request.getParameter(POST_USERNAME);
        if (paramUserName == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "user name required");
            return;
        }
        if (!App.isValidUserName(paramUserName)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid name");//????
            return;
        }
        String paramP1 = request.getParameter(POST_PASSWORD1);
        if (paramP1 == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "password (1) required");
            return;
        }
        String paramP2 = request.getParameter(POST_PASSWORD2);
        if (paramP2 == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "password (2) required");
            return;
        }
        if (!paramP1.equals(paramP2)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "passwords don't match");//?
            return;
        }
        String passwordHash = App.passwordToHash(paramP1);
        byte[] userSecret = Secret.create(passwordHash.toCharArray(), 1).getData();
        boolean success = false;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Token token = em.find(Token.class, tokenId);
            if (token != null &&
                    token.getAction() == TokenAction.ACTIVATE_ACCOUNT &&
                    Secret.check(token.getSecret(), paramTokenValue)) {
                User u = token.getUser();
                if (!u.isActivated()) {
                    u.setName(paramUserName);
                    u.setSecret(userSecret);
                    u.setActivated(true);
                    u.setBlocked(false);
                    em.merge(u);
                    em.remove(token);
                    success = true;
                }
            }

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        if (!success) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//???
            return;
        }

        request.getRequestDispatcher("/WEB-INF/view/ActivateAccount_Post.jsp").include(request, response);
    }
}
