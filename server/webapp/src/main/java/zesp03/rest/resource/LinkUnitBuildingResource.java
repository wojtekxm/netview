package zesp03.rest.resource;

import zesp03.common.Database;
import zesp03.data.row.LinkUnitBuildingRow;
import zesp03.entity.Link_unit_building;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;

/**
 * Created by Berent on 2017-03-06.
 */

@Path("link-unit-building")
public class LinkUnitBuildingResource {
    @GET
    @Produces("application/json")
    public LinkUnitBuildingRow getLinkUnitBuilding(
            @QueryParam("id") long id) {
        LinkUnitBuildingRow result = null;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Link_unit_building lub = em.find(Link_unit_building.class, id);
            if (lub != null)
                result = new LinkUnitBuildingRow(lub);

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
/*
    @POST
    @Consumes()
    public void add(LinkUnitBuildingRow huhu)
*/
}
