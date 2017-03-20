package zesp03.webapp.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.core.Database;
import zesp03.common.entity.Building;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.BuildingUnitsControllersDto;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.dto.UnitDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BuildingApi {
    @GetMapping("/api/all-buildings")
    public List<BuildingDto> getAllBuildings() {
        List<BuildingDto> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT b FROM Building b", Building.class)
                    .getResultList()
                    .stream()
                    .map(BuildingDto::make)
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

    @GetMapping("/api/building")
    public BuildingUnitsControllersDto getBuilding(
            @RequestParam("id") long id ) {

        BuildingUnitsControllersDto result;

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

            ControllerDto controller;
            BuildingDto building = null;
            UnitDto unit;

            ArrayList<UnitDto> units = new ArrayList<>();
            List <ControllerDto> controllers = new ArrayList<>();

            // list.get(0)[0] = id
            // list.get(0)[1] = code
            // list.get(0)[2] = name
            // list.get(0)[3] = latitude
            // list.get(0)[4] = longitude
            if( !list.isEmpty() ) {
                building = new BuildingDto();
                building.setId(((Number) list.get(0)[0]).longValue());
                building.setCode(list.get(0)[1].toString());
                building.setName(list.get(0)[2].toString());
                building.setLatitude(((BigDecimal) list.get(0)[3]).doubleValue());
                building.setLongitude(((BigDecimal) list.get(0)[4]).doubleValue());
            }

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

                    unit = new UnitDto();
                    unit.setId((long)object[5]);
                    unit.setCode(object[6].toString());
                    unit.setDescription(object[7].toString());
                    if( !units.contains( unit ) )
                        units.add( unit );
                }

                if( object[8] != null ) {

                    controller = new ControllerDto();
                    controller.setId((long)object[8]);
                    controller.setName(object[9].toString());
                    controller.setIpv4(object[10].toString());
                    controller.setDescription(object[11] == null ? null : object[11].toString());
                    controller.setBuildingId((long)object[12]);
                    if( !controllers.contains( controller ) )
                        controllers.add( controller );
                }
            }

            result = new BuildingUnitsControllersDto();
            result.setBuilding(building);
            result.setUnits(units);
            result.setControllers(controllers);

            tran.commit();

        } catch (RuntimeException exc) {

            if( tran != null && tran.isActive() )
                tran.rollback();

            throw exc;

        } finally {

            if( em != null )
                em.close();
        }

        return result;
    }
}
