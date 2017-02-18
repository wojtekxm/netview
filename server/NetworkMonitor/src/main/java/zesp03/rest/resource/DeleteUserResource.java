package zesp03.rest.resource;

import zesp03.core.Database;
import zesp03.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;

@Path("delete-user")
public class DeleteUserResource {
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public boolean deleteUser(@FormParam("id") long id) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = em.find(User.class, id);
            if (u == null) throw new NotFoundException();
            em.remove(u);

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
        return true;
    }
}
