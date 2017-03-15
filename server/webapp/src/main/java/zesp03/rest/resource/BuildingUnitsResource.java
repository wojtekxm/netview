package zesp03.rest.resource;

import zesp03.common.Database;
import zesp03.data.BuildingUnitsData;
import zesp03.data.row.BuildingRow;
import zesp03.data.row.UnitRow;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Berent on 2017-03-13.
 */

@Path("building")
public class BuildingUnitsResource {

    @GET
    @Produces("application/json")
    public BuildingUnitsData getBuilding( @QueryParam("id") long id ) {

        BuildingUnitsData result = null;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {

            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List< Object[] > list = em.createQuery( "SELECT b.id, b.code, b.name, b.latitude, b.longitude, u.id, u.code, u.description " +
                    "FROM Link_unit_building lub " +
                    "INNER JOIN Building b ON lub.building.id = " + id +
                    " AND lub.building.id = b.id " +
                    "INNER JOIN Unit u ON lub.unit.id = u.id", Object[].class).getResultList();

            List<UnitRow> units = new ArrayList<>();
            BuildingRow building = new BuildingRow();
            // list.get(0)[0] = id
// list.get(0)[1] = code
// list.get(0)[2] = name
// list.get(0)[3] = latitude
// list.get(0)[4] = longitude
            if( !list.isEmpty() )
                building = new BuildingRow( ((Number)list.get(0)[0]).longValue(), list.get(0)[1].toString(), list.get(0)[2].toString(), (BigDecimal) list.get(0)[3], (BigDecimal) list.get(0)[4] );

// object[5] = id
// object[6] = code
// object[7] = description
            for( Object[] object : list )
                units.add( new UnitRow( ((Number)object[5]).longValue(), object[6].toString(), object[7].toString() ) );


            result = new BuildingUnitsData( building, units );

            tran.commit();

        } catch (RuntimeException exc) {

            if( tran != null && tran.isActive() )
                tran.rollback();

            throw exc;

        } finally {

            if( em != null )
                em.close();
        }

        if (result == null)
            throw new NotFoundException();

        return result;
    }
}