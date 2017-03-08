package zesp03.rest.resource;

import zesp03.common.Database;
import zesp03.data.row.LinkUnitBuildingRow;
import zesp03.entity.Link_unit_building;

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

@Path("all-link-units-buildings")
public class AllLinkUnitsBuildingsResource {
    @GET
    @Produces("application/json")
    public List<LinkUnitBuildingRow> getAllLinkUnitsBuildings() {
        List<LinkUnitBuildingRow> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT lub FROM Link_unit_building lub ", Link_unit_building.class)
                    .getResultList()
                    .stream()
                    .map(LinkUnitBuildingRow::new)
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
