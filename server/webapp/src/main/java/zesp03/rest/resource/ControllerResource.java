package zesp03.rest.resource;

import zesp03.common.Database;
import zesp03.data.row.ControllerRow;
import zesp03.entity.Controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;

@Path("controller")
public class ControllerResource {
    @GET
    @Produces("application/json")
    public ControllerRow getController(
            @QueryParam("id") long id) {
        ControllerRow result = null;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Controller c = em.find(Controller.class, id);
            if (c != null)
                result = new ControllerRow(c);

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
