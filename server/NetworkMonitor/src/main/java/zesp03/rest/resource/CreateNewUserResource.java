package zesp03.rest.resource;

import zesp03.core.App;
import zesp03.core.Database;
import zesp03.core.Secret;
import zesp03.entity.Token;
import zesp03.entity.TokenAction;
import zesp03.entity.User;
import zesp03.entity.UserRole;
import zesp03.rest.response.CreateNewUserResponse;
import zesp03.servlet.ActivateAccountServlet;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@Path("create-new-user")
@Consumes("application/x-www-form-urlencoded")
@Produces("application/json")
public class CreateNewUserResource {
    @POST
    public CreateNewUserResponse createNewUser(@FormParam("is-admin") boolean isAdmin, @Context HttpServletRequest request) {
        CreateNewUserResponse result = new CreateNewUserResponse();
        result.setSuccess(false);

        String tokenValue = App.generateToken(32);
        Secret secret = Secret.create(tokenValue.toCharArray(), 1);

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = new User();
            u.setName(null);
            u.setSecret(null);
            u.setActivated(false);
            u.setBlocked(true);
            u.setRole(isAdmin ? UserRole.ADMIN : UserRole.NORMAL);
            em.persist(u);

            Token t = new Token();
            t.setSecret(secret.getData());
            t.setAction(TokenAction.ACTIVATE_ACCOUNT);
            t.setUser(u);
            em.persist(t);
            result.setTokenId(t.getId());
            result.setTokenValue(tokenValue);
            result.setUserId(u.getId());
            result.setSuccess(true);

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        result.setActivationURL(
                new StringBuilder()
                        .append("http://")
                        .append(request.getServerName())
                        .append(request.getServerPort() != 80 ? ":" + request.getServerPort() : "")
                        .append("/activate-account?")
                        .append(ActivateAccountServlet.GET_TOKEN_ID)
                        .append("=")
                        .append(result.getTokenId())
                        .append("&")
                        .append(ActivateAccountServlet.GET_TOKEN_VALUE)
                        .append("=")
                        .append(result.getTokenValue())
                        .toString()
        );
        return result;
    }
}