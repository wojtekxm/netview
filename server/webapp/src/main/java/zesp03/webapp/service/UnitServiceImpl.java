package zesp03.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import zesp03.webapp.dto.input.CreateUserDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public void create(CreateUserDto dto) {
        if(dto.getCode() == null) {
            throw new ValidationException("code", "null");
        }
        //TODO co jak o takim kodzie już istnieje
        Unit unit = new Unit();
        unit.setCode(dto.getCode());
        unit.setDescription(dto.getDescription());
        unitRepository.save(unit);
    }

    @Override
    public void remove(Long unitId) {
        Unit u = unitRepository.findOne(unitId);
        if(u == null) {
            throw new NotFoundException("unit");
        }
        unitRepository.delete(u);
    }

    @Override
    public UnitDto modify(long id) {
        Unit u = em.find(Unit.class, id);
        if(u == null)
            throw new NotFoundException("modify-unit");
        return UnitDto.make(u);
    }

    @Override
    public void acceptModifyUnit(long id, String code, String description) {
        Unit u= em.find(Unit.class, id);
        if(u == null) {
            if (em != null)
                em.close();
            throw new NotFoundException("unit");
        }
        u.setCode(code);
        u.setDescription(description);
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
    public UnitBuildingsDto getOne(long id) {
        UnitBuildingsDto result;
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
        return result;
    }
}
