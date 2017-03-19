package zesp03.rest.resource;

/**
 * Created by lektor on 2017-03-08.
 */

import zesp03.common.Database;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import javax.ws.rs.*;
import java.util.*;

@Path("/chart")
@Produces("application/json")

// WYPOCINY LEKTORA
public class ChartResource {
    @GET
    public List<Object[]> getDev(
            @QueryParam("id") long id
            /*@QueryParam("timestamp1") int timestamp1,
            @QueryParam("timestamp2") int timestamp2*/
 ) {
        List<Object[]> results=new ArrayList<Object[]>();
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();
            results = em.createQuery(" SELECT cs.clientsSum,cs.timestamp FROM DeviceSurvey cs " +
                  "WHERE cs.device.id= :id "
                    /* AND (cs(timestamp) BETWEEN :timestamp1 AND  :timestamp2 */
                    , Object[].class)
                    .setParameter("id", id)
                    /*.setParameter("timestamp1", timestamp1)
                    .setParameter("timestamp2", timestamp2)*/
                    .getResultList();
            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
        return results;
    }
}
