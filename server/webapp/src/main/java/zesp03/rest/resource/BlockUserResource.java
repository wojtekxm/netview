package zesp03.rest.resource;

import zesp03.common.Database;
import zesp03.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;

@Path("block-user")
public class BlockUserResource {
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public boolean blockUser(
            @FormParam("id") long id) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = em.find(User.class, id);
            if (u == null) throw new NotFoundException();
            u.setBlocked(true);
            em.merge(u);

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
