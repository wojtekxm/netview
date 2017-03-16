package zesp03.rest.resource;

import zesp03.common.Database;
import zesp03.data.BuildingUnitsControllersData;
import zesp03.data.row.BuildingRow;
import zesp03.data.row.ControllerRow;
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
public class BuildingUnitsControllersResource {

    @GET
    @Produces("application/json")
    public BuildingUnitsControllersData getBuilding( @QueryParam("id") long id ) {

        BuildingUnitsControllersData result = null;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {

            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List< Object[] > list = em.createQuery( "SELECT b.id, b.code, b.name, b.latitude, b.longitude, u.id, u.code, u.description, c.id, c.name, c.ipv4, c.description, c.building.id " +
                    "FROM LinkUnitBuilding lub " +
                    "INNER JOIN Building b ON lub.building.id = " + id +
                    " AND lub.building.id = b.id " +
                    "INNER JOIN Unit u ON lub.unit.id = u.id " +
                    "LEFT JOIN Controller c ON c.building.id = b.id ", Object[].class).getResultList();

            ControllerRow controller = null;
            BuildingRow building = null;
            UnitRow unit = null;

            List<UnitRow> units = new ArrayList<>();
            List <ControllerRow> controllers = new ArrayList<>();

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
            // object[8] = id
            // object[9] = name
            // object[10] = ipv4
            // object[11] = description
            // object[12] = building_id
            for( Object[] object : list ) {

                controller = null;
                unit = null;

                if( object[5] != null ) {

                    unit = new UnitRow( ((long)object[5]), object[6].toString(), object[7].toString() );
                    if( !units.contains( unit ) )
                        units.add( unit );
                }

                if( object[8] != null ) {

                    controller = new ControllerRow( ((long)object[8]), object[9].toString(), object[10].toString(),  object[11] == null ? "" : object[11].toString(),((long)object[12]) );
                    if( !controllers.contains( controller ) )
                        controllers.add( controller );
                }
            }

            result = new BuildingUnitsControllersData( building, units, controllers );

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