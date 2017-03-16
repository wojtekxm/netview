package zesp03.rest.resource;

import zesp03.common.Database;
import zesp03.data.UnitBuildingsData;
import zesp03.data.row.BuildingRow;
import zesp03.data.row.UnitRow;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Berent on 2017-03-15.
 */

    @Path("unit")
    public class UnitBuildingsResource {

        @GET
        @Produces("application/json")
        public UnitBuildingsData getUnit(@QueryParam("id") long id ) {

            UnitBuildingsData result = null;

            EntityManager em = null;
            EntityTransaction tran = null;
            try {

                em = Database.createEntityManager();
                tran = em.getTransaction();
                tran.begin();

                List< Object[] > list = em.createQuery( "SELECT u.id, u.code, u.description, b.id, b.code, b.name, b.latitude, b.longitude " +
                        "FROM LinkUnitBuilding lub " +
                        "INNER JOIN Unit u ON lub.unit.id = " + id +
                        " AND lub.unit.id = u.id " +
                        "INNER JOIN Building b ON lub.building.id = b.id", Object[].class).getResultList();

                List<BuildingRow> buildings= new ArrayList<>();
                UnitRow unit = new UnitRow();
                // list.get(0)[0] = id
// list.get(0)[1] = code
// list.get(0)[2] = description


                if( !list.isEmpty() )
                    unit = new UnitRow( ((Number)list.get(0)[0]).longValue(), list.get(0)[1].toString(), list.get(0)[2].toString() );
// object[3] = id
// object[4] = code
// object[5] = name
// object[6] = latitude
// object[7] = longitude

                for( Object[] object : list )
                    buildings.add( new BuildingRow ( ((Number)object[3]).longValue(), object[4].toString(), object[5].toString(), (BigDecimal)object[6], (BigDecimal)object[7] ));




                result = new UnitBuildingsData( unit, buildings );

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
