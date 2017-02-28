package zesp03.rest.resource;

import zesp03.common.Database;
import zesp03.data.UserData;
import zesp03.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.stream.Collectors;

@Path("all-users")
public class AllUsersResource {
    @GET
    @Produces("application/json")
    public List<UserData> getAllUsers() {
        List<UserData> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT u FROM User u", User.class)
                    .getResultList()
                    .stream()
                    .map(UserData::new)
                    .collect(Collectors.toList());

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        return list;
    }
}
