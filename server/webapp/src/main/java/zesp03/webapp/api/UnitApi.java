package zesp03.webapp.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.core.Database;
import zesp03.common.entity.LinkUnitBuilding;
import zesp03.common.entity.Unit;
import zesp03.common.exception.NotFoundException;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.LinkUnitBuildingDto;
import zesp03.webapp.dto.UnitBuildingsDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.dto.result.BaseResultDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UnitApi {
    @GetMapping("/api/all-units")
    public List<UnitDto> getAllUnits() {
        List<UnitDto> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT u FROM Unit u", Unit.class)
                    .getResultList()
                    .stream()
                    .map(UnitDto::make)
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
    public UnitBuildingsDto getUnit(
            @RequestParam("id") long id ) {

        UnitBuildingsDto result;

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

            List<BuildingDto> buildings= new ArrayList<>();
            UnitDto unit = new UnitDto();
            // list.get(0)[0] = id
// list.get(0)[1] = code
// list.get(0)[2] = description


            if( !list.isEmpty() ) {
                unit = new UnitDto();
                unit.setId(((Number)list.get(0)[0]).longValue());
                unit.setCode(list.get(0)[1].toString());
                unit.setDescription(list.get(0)[2].toString());
            }
// object[3] = id
// object[4] = code
// object[5] = name
// object[6] = latitude
// object[7] = longitude

            for( Object[] object : list ) {
                BuildingDto dto = new BuildingDto();
                dto.setId(((Number)object[3]).longValue());
                dto.setCode(object[4].toString());
                dto.setName(object[5].toString());
                dto.setLatitude( ((BigDecimal)object[6]).doubleValue() );
                dto.setLongitude( ((BigDecimal)object[7]).doubleValue() );
            }



            result = new UnitBuildingsDto();
            result.setUnit(unit);
            result.setBuildings(buildings);

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

    @GetMapping("/api/link-unit-building")
    public LinkUnitBuildingDto getLinkUnitBuilding(
            @RequestParam("id") long id) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            LinkUnitBuilding lub = em.find(LinkUnitBuilding.class, id);
            if(lub == null)
                throw new NotFoundException("link-unit-building");
            LinkUnitBuildingDto dto = LinkUnitBuildingDto.make(lub);
            tran.commit();
            return dto;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @GetMapping("/api/all-link-units-buildings")
    public List<LinkUnitBuildingDto> getAllLinkUnitsBuildings() {
        List<LinkUnitBuildingDto> list;

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            list = em.createQuery("SELECT lub FROM LinkUnitBuilding lub ", LinkUnitBuilding.class)
                    .getResultList()
                    .stream()
                    .map(LinkUnitBuildingDto::make)
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

    @PostMapping(value = "/api/create-unit", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto create(
            @RequestParam("code") String code,
            @RequestParam(value = "description", required = false) String description) {


        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Unit unit = new Unit();
            unit.setCode(code);
            unit.setDescription(description);

            em.persist(unit);
            tran.commit();
            return new BaseResultDto(true);
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    @PostMapping(value = "/api/remove-unit", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto remove(
            @RequestParam("id") long id) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Unit u = em.find(Unit.class, id);
            if (u == null)
                throw new NotFoundException("unit");
            em.remove(u);
            tran.commit();
            return new BaseResultDto(true);
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

}
