package zesp03.rest.resource;

import zesp03.common.Database;
import zesp03.data.row.ControllerRow;
import zesp03.entity.Controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.stream.Collectors;

@Path("all-controllers")
public class AllControllersResource {
    @GET
    @Produces("application/json")
    public List<ControllerRow> getAllControllers() {
        List<ControllerRow> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT c FROM Controller c", Controller.class)
                    .getResultList()
                    .stream()
                    .map(ControllerRow::new)
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