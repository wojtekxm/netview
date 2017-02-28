package zesp03.rest.resource;

import zesp03.common.Database;
import zesp03.data.UserData;
import zesp03.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;

@Path("user")
public class UserResource {
    @GET
    @Produces("application/json")
    public UserData getUser(
            @QueryParam("id") long id) {
        UserData result = null;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = em.find(User.class, id);
            if (u != null)
                result = new UserData(u);

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        if (result == null)
            throw new NotFoundException();
        return result;
    }
}
