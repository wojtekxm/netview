package zesp03.rest.resource;

import zesp03.common.Database;
import zesp03.data.row.UnitRow;
import zesp03.entity.Unit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Berent on 2017-03-06.
 */

@Path("all-units")
public class AllUnitsResource {
    @GET
    @Produces("application/json")
    public List<UnitRow> getAllUnits() {
        List<UnitRow> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT u FROM Unit u", Unit.class)
                    .getResultList()
                    .stream()
                    .map(UnitRow::new)
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
