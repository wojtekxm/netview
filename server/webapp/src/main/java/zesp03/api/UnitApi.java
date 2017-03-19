package zesp03.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.Database;
import zesp03.data.UnitBuildingsData;
import zesp03.data.row.BuildingRow;
import zesp03.data.row.LinkUnitBuildingRow;
import zesp03.data.row.UnitRow;
import zesp03.entity.LinkUnitBuilding;
import zesp03.entity.Unit;
import zesp03.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UnitApi {
    @GetMapping("/api/all-units")
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

    @GetMapping("/api/unit")
    public UnitBuildingsData getUnit(
            @RequestParam("id") long id ) {

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
            throw new NotFoundException("");

        return result;
    }

    @GetMapping("/api/link-unit-building")
    public LinkUnitBuildingRow getLinkUnitBuilding(
            @RequestParam("id") long id) {
        LinkUnitBuildingRow result = null;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            LinkUnitBuilding lub = em.find(LinkUnitBuilding.class, id);
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
            throw new NotFoundException("");
        return result;
    }

    @GetMapping("/api/all-link-units-buildings")
    public List<LinkUnitBuildingRow> getAllLinkUnitsBuildings() {
        List<LinkUnitBuildingRow> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT lub FROM LinkUnitBuilding lub ", LinkUnitBuilding.class)
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
