package zesp03.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.entity.Building;
import zesp03.common.entity.LinkUnitBuilding;
import zesp03.common.entity.Unit;
import zesp03.common.exception.NotFoundException;
import zesp03.common.exception.ValidationException;
import zesp03.common.repository.LinkUnitBuildingRepository;
import zesp03.common.repository.UnitRepository;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.LinkUnitBuildingDto;
import zesp03.webapp.dto.UnitBuildingsDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.dto.input.CreateUnitDto;
import zesp03.webapp.dto.result.BaseResultDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UnitServiceImpl implements UnitService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private LinkUnitBuildingRepository linkUnitBuildingRepository;

    @Override
    public List<UnitDto> getAll() {
        List<UnitDto> list = new ArrayList<>();
        for(Unit u : unitRepository.findAll()) {
            list.add( UnitDto.make(u) );
        }
        return list;
    }

    @Override
    public UnitDto getOne(long unitId) {
        Unit u = unitRepository.findOne(unitId);
        if(u == null) {
            throw new NotFoundException("unit");
        }
        return UnitDto.make(u);
    }

    @Override
    public UnitBuildingsDto linkUnitAllBuildings(long unitId) {
        UnitBuildingsDto unit_building_dto = new UnitBuildingsDto ();
        List< BuildingDto > buildings = em.createQuery("SELECT b FROM Building b", Building.class)
                .getResultList()
                .stream()
                .map(BuildingDto::make)
                .collect(Collectors.toList());

        Unit u = em.find(Unit.class, unitId);
        if(u == null)
            throw new NotFoundException("link-unit-all-buildings");

        unit_building_dto.setUnit( UnitDto.make( u ) );
        unit_building_dto.setBuildings( buildings );
        return unit_building_dto;
    }

    @Override
    public List<BuildingDto> listForLinkUnitAllBuildings(long unitId) {
        return em.createQuery("SELECT b " +
                "FROM Building b " +
                "WHERE b.id NOT IN " +
                "( SELECT lub.building.id FROM LinkUnitBuilding lub " +
                "WHERE lub.unit.id = "+ unitId +")", zesp03.common.entity.Building.class)
                .getResultList()
                .stream()
                .map( BuildingDto::make )
                .collect( Collectors.toList() );
    }

    @Override
    public void create(CreateUnitDto dto) {
        if(dto.getCode() == null || dto.getCode().isEmpty()) {
            throw new ValidationException("code", "null");
        }
        if(dto.getDescription() == null || dto.getDescription().isEmpty()) {
            throw new ValidationException("description", "null");
        }

        //TODO co jak o takim kodzie już istnieje
        Unit unit = new Unit();
        unit.setCode(dto.getCode());
        unit.setDescription(dto.getDescription());
        unitRepository.save(unit);
    }

    @Override
    public void removeUnit(Long unitId) {
        Unit u = unitRepository.findOne(unitId);
        if(u == null) {
            throw new NotFoundException("unit");
        }
        unitRepository.delete(u);
    }

    @Override
    public UnitDto modifyUnit(long id) {
        Unit u = em.find(Unit.class, id);
        if(u == null)
            throw new NotFoundException("modify-unit");
        return UnitDto.make(u);
    }

    @Override
    public void acceptModifyUnit(UnitDto dto) {

        if(dto.getCode() == null || dto.getCode().isEmpty()) {
            throw new ValidationException("code", "null");
        }
        if(dto.getDescription() == null || dto.getDescription().isEmpty()) {
            throw new ValidationException("description", "null");
        }

        Unit u = unitRepository.findOne(dto.getId());

        u.setCode(dto.getCode());
        u.setDescription(dto.getDescription());

        unitRepository.save(u);

    }



    @Override
    public LinkUnitBuildingDto getLinkUnitBuilding(long id) {
        LinkUnitBuilding lub = linkUnitBuildingRepository.findOne(id);
        if(lub == null) {
            throw new NotFoundException("link-unit-building");
        }
        return LinkUnitBuildingDto.make(lub);
    }

    @Override
    public List<LinkUnitBuildingDto> getAllLinkUnitBuildings() {
        List<LinkUnitBuildingDto> list = new ArrayList<>();
        for( LinkUnitBuilding lub : linkUnitBuildingRepository.findAll() ) {
            list.add( LinkUnitBuildingDto.make(lub) );
        }
        return list;
    }

    @Override
    public UnitBuildingsDto getUnitBuildings(long unitId) {
        List< Object[] > list = em.createQuery( "SELECT u.id, u.code, u.description, b.id, b.code, b.name, b.latitude, b.longitude " +
                "FROM LinkUnitBuilding lub " +
                "INNER JOIN Unit u ON lub.unit.id = " + unitId +
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
            dto.setLatitude( ((BigDecimal)object[6]));//.doubleValue() );
            dto.setLongitude( ((BigDecimal)object[7]));//.doubleValue() );
        }



        UnitBuildingsDto result = new UnitBuildingsDto();
        result.setUnit(unit);
        result.setBuildings(buildings);
        return result;
    }

    @Override
    public void connect(Long buildingId, Long unitId) {
        LinkUnitBuilding linkunitbuilding = new LinkUnitBuilding();
        Unit unit = em.find(Unit.class, unitId);
        Building building = em.find(Building.class, buildingId);

        linkunitbuilding.setBuilding(building);
        linkunitbuilding.setUnit(unit);
        em.persist(linkunitbuilding);
    }

    @Override
    public List<BuildingDto> LinkUnitBuildingPage_GET_link_unit_all_buildings(long id) {
        return em.createQuery("SELECT b " +
                "FROM Building b " +
                "WHERE b.id NOT IN " +
                "( SELECT lub.building.id FROM LinkUnitBuilding lub " +
                "WHERE lub.unit.id = "+ id +")", zesp03.common.entity.Building.class)
                .getResultList()
                .stream()
                .map( BuildingDto::make )
                .collect( Collectors.toList() );
    }

    @Override
    public void LinkUnitBuildingPage_GET_remove_unit_buildings(long id_unit, long id_building) {
        em.createQuery("DELETE FROM LinkUnitBuilding lub " +
                "WHERE lub.building.id = " + id_building +
                " AND lub.unit.id = " + id_unit + " " ).executeUpdate();
    }

    @Override
    public List<UnitDto> LinkUnitBuildingPage_GET_link_building_all_units(long id) {
        return em.createQuery("SELECT u " +
                "FROM Unit u " +
                "WHERE u.id NOT IN " +
                "( SELECT lub.unit.id FROM LinkUnitBuilding lub " +
                "WHERE lub.building.id = "+ id +")", zesp03.common.entity.Unit.class)
                .getResultList()
                .stream()
                .map( UnitDto::make )
                .collect( Collectors.toList() );
    }

    @Override
    public void LinkUnitBuildingPage_GET_link_building_units(long id_unit, long id_building) {
        LinkUnitBuilding linkunitbuilding = new LinkUnitBuilding();
        Building building = em.find(Building.class, id_building);
        Unit unit = em.find(Unit.class, id_unit);



        linkunitbuilding.setUnit(unit);
        linkunitbuilding.setBuilding(building);
        em.persist(linkunitbuilding);
    }

    @Override
    public List<UnitDto> LinkUnitBuildingPage_GET_remove_building_all_units(long buildingId) {
        return em.createQuery( "SELECT u " +
                "FROM LinkUnitBuilding lub " +
                "INNER JOIN Building b ON (lub.building.id = " + buildingId +
                " AND lub.building.id = b.id)" +
                "INNER JOIN Unit u ON lub.unit.id = u.id", zesp03.common.entity.Unit.class)
                .getResultList()
                .stream()
                .map( UnitDto::make )
                .collect( Collectors.toList() );
    }

    @Override
    public void LinkUnitBuildingPage_GET_remove_buildings_units(long id_building, long id_unit) {
        em.createQuery("DELETE FROM LinkUnitBuilding lub " +
                "WHERE lub.unit.id = " + id_unit +
                " AND lub.building.id = " + id_building + " " ).executeUpdate();
    }

    @Override
    public List<BuildingDto> UnitPage_GET_unit(long id) {
        return em.createQuery( "SELECT b " +
                "FROM LinkUnitBuilding lub " +
                "INNER JOIN Unit u ON (lub.unit.id = " + id +
                " AND lub.unit.id = u.id)" +
                "INNER JOIN Building b ON lub.building.id = b.id", zesp03.common.entity.Building.class)
                .getResultList()
                .stream()
                .map( BuildingDto::make )
                .collect( Collectors.toList() );
    }
}
