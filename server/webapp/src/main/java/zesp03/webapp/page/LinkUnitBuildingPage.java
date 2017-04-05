package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.core.Database;
import zesp03.common.entity.Building;
import zesp03.common.entity.LinkUnitBuilding;
import zesp03.common.entity.Unit;
import zesp03.common.exception.NotFoundException;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.UnitDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LinkUnitBuildingPage {
    @GetMapping("/unitsbuildings")
    public String get(ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;


        List<LinkUnitBuilding> BuildingList;

        try {

            em = Database.createEntityManager();
            tran = em.getTransaction();

            tran.begin();

            BuildingList = em.createQuery("SELECT lub FROM LinkUnitBuilding lub", LinkUnitBuilding.class).getResultList();
            tran.commit();

        } catch (RuntimeException exc) {
            if( tran != null && tran.isActive() )
                tran.rollback();
            throw exc;
        } finally {
            if (em != null)
                em.close();
        }

        model.put("list", BuildingList);
        return "link-unit-building";
    }

    @GetMapping("/link-unit-all-buildings")
    public String get(@RequestParam("id") long id,
                      ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;

        try {

            em = Database.createEntityManager();
            tran = em.getTransaction();

            tran.begin();
            Unit u = em.find(Unit.class, id);
            List<BuildingDto> b1 = em.createQuery("SELECT b " +
                    "FROM Building b " +
                    "WHERE b.id NOT IN " +
                    "( SELECT lub.building.id FROM LinkUnitBuilding lub " +
                    "WHERE lub.unit.id = "+ id +")", zesp03.common.entity.Building.class)
                    .getResultList()
                    .stream()
                    .map( BuildingDto::make )
                    .collect( Collectors.toList() );


            model.put("unit", u);
            model.put("buildings",b1);
            tran.commit();
        } catch (RuntimeException exc) {
            if( tran != null && tran.isActive() )
                tran.rollback();
            throw exc;
        } finally {
            if (em != null)
                em.close();
        }
        return "link-unit-all-buildings";
    }

    @GetMapping("/link-unit-buildings")
    public String get(
            @RequestParam("id_building") long id_building,
            @RequestParam("id_unit") long id_unit ,
            ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            LinkUnitBuilding linkunitbuilding = new LinkUnitBuilding();
            Unit unit = em.find(Unit.class, id_unit);
            Building building = em.find(Building.class, id_building);

            linkunitbuilding.setBuilding(building);
            linkunitbuilding.setUnit(unit);
            em.persist(linkunitbuilding);
            tran.commit();

        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
        return  get( id_unit, model);
    }


    @GetMapping("/remove-unit-all-buildings")
    public String getRemove(
            @RequestParam("id") long id,
            ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            zesp03.common.entity.Unit u = em.find( zesp03.common.entity.Unit.class, id );
            if(u == null)
                throw new NotFoundException("unit");

            List< BuildingDto > b = em.createQuery( "SELECT b " +
                    "FROM LinkUnitBuilding lub " +
                    "INNER JOIN Unit u ON (lub.unit.id = " + id +
                    " AND lub.unit.id = u.id)" +
                    "INNER JOIN Building b ON lub.building.id = b.id", zesp03.common.entity.Building.class)
                    .getResultList()
                    .stream()
                    .map( BuildingDto::make )
                    .collect( Collectors.toList() );

            if(b == null)
                throw new NotFoundException( "buildings" );

            tran.commit();

            model.put( "unit", UnitDto.make(u) );
            model.put( "buildings", b );
            return "remove-unit-all-buildings";

        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }


    @GetMapping("/remove-unit-buildings")
    public String remove(
            @RequestParam("id_unit") long id_unit ,
            @RequestParam("id_building") long id_building,
            ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            em.createQuery("DELETE FROM LinkUnitBuilding lub " +
                    "WHERE lub.building.id = " + id_building +
                    " AND lub.unit.id = " + id_unit + " " ).executeUpdate();


                tran.commit();

        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }


        return getRemove( id_unit, model );

    }
//---------------------------------------------------------------------------------




@GetMapping("/link-building-all-units")
public String getb(@RequestParam("id") long id,
                  ModelMap model) {
    EntityManager em = null;
    EntityTransaction tran = null;

    try {

        em = Database.createEntityManager();
        tran = em.getTransaction();

        tran.begin();
        Building b= em.find(Building.class, id);
        List<UnitDto> u1 = em.createQuery("SELECT u " +
                "FROM Unit u " +
                "WHERE u.id NOT IN " +
                "( SELECT lub.unit.id FROM LinkUnitBuilding lub " +
                "WHERE lub.building.id = "+ id +")", zesp03.common.entity.Unit.class)
                .getResultList()
                .stream()
                .map( UnitDto::make )
                .collect( Collectors.toList() );


        model.put("building", b);
        model.put("units",u1);
        tran.commit();
    } catch (RuntimeException exc) {
        if( tran != null && tran.isActive() )
            tran.rollback();
        throw exc;
    } finally {
        if (em != null)
            em.close();
    }
    return "link-building-all-units";
}

    @GetMapping("/link-building-units")
    public String getb(
            @RequestParam("id_unit") long id_unit ,
            @RequestParam("id_building") long id_building,
            ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            LinkUnitBuilding linkunitbuilding = new LinkUnitBuilding();
            Building building = em.find(Building.class, id_building);
            Unit unit = em.find(Unit.class, id_unit);



            linkunitbuilding.setUnit(unit);
            linkunitbuilding.setBuilding(building);
            em.persist(linkunitbuilding);
            tran.commit();

        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
        return  getb( id_building, model);
    }


    @GetMapping("/remove-building-all-units")
    public String getRemoveB(
            @RequestParam("id") long id,
            ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            zesp03.common.entity.Building b = em.find( zesp03.common.entity.Building.class, id );
            if(b == null)
                throw new NotFoundException("building");

            List< UnitDto > u = em.createQuery( "SELECT u " +
                    "FROM LinkUnitBuilding lub " +
                    "INNER JOIN Building b ON (lub.building.id = " + id +
                    " AND lub.building.id = b.id)" +
                    "INNER JOIN Unit u ON lub.unit.id = u.id", zesp03.common.entity.Unit.class)
                    .getResultList()
                    .stream()
                    .map( UnitDto::make )
                    .collect( Collectors.toList() );

            if(u == null)
                throw new NotFoundException( "units" );

            tran.commit();

            model.put( "building", BuildingDto.make(b) );
            model.put( "units", u );
//ktory jsp wywolac
            return "remove-building-all-units";

        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }


    @GetMapping("/remove-building-units")
    public String removeB(
            @RequestParam("id_building") long id_building,
            @RequestParam("id_unit") long id_unit ,
            ModelMap model) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            em.createQuery("DELETE FROM LinkUnitBuilding lub " +
                    "WHERE lub.unit.id = " + id_unit +
                    " AND lub.building.id = " + id_building + " " ).executeUpdate();


            tran.commit();

        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }

        return getRemoveB( id_building, model );

    }
}
