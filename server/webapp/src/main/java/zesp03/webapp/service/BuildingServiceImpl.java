/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.entity.Building;
import zesp03.common.entity.Controller;
import zesp03.common.exception.NotFoundException;
import zesp03.common.exception.ValidationException;
import zesp03.common.repository.BuildingRepository;
import zesp03.common.repository.DeviceRepository;
import zesp03.common.repository.LinkUnitBuildingRepository;
import zesp03.webapp.dto.*;
import zesp03.webapp.dto.input.CreateBuildingDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BuildingServiceImpl implements BuildingService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private LinkUnitBuildingRepository linkUnitBuildingRepository;

    @Override
    public List<BuildingDto> getAllBuildings() {
        return em.createQuery("SELECT b FROM Building b", Building.class)
                .getResultList()
                .stream()
                .map(BuildingDto::make)
                .collect(Collectors.toList());
    }

    @Override
    public BuildingDto getOneBuilding(long id) {
        Building b = em.find(Building.class, id);
        if(b == null)
            throw new NotFoundException("building");
        return BuildingDto.make(b);
    }

    @Override
    public BuildingDetailsDto getDetailsOne(Long buildingId) {
        Building b = buildingRepository.findOne(buildingId);
        if(b == null)
            throw new NotFoundException("building");
        return BuildingDetailsDto.make(b);
    }

    @Override
    public List<UnitDto> getUnits(Long buildingId) {
        Building b = buildingRepository.findOne(buildingId);
        if(b == null)
            throw new NotFoundException("building");
        return b.getLubList()
                .stream()
                .map( lub -> UnitDto.make(lub.getUnit()) )
                .collect(Collectors.toList());
    }

    @Override
    public List<ControllerDto> getControllersInfo(Long buildingId) {
        return em.createQuery("SELECT c FROM Controller c " +
                        "WHERE c.building.id = :bid",
                Controller.class)
                .setParameter("bid", buildingId)
                .getResultList()
                .stream()
                .map(ControllerDto::make)
                .collect(Collectors.toList());
    }

    @Override
    public List<ControllerDetailsDto> getControllersDetails(Long buildingId) {
        //TODO sprawdź wydajność SQL
        return em.createQuery("SELECT c FROM Controller c " +
                        "LEFT JOIN FETCH c.building " +
                        "WHERE c.building.id = :bid",
                Controller.class)
                .setParameter("bid", buildingId)
                .getResultList()
                .stream()
                .map( c -> ControllerDetailsDto.make(c,
                        deviceRepository.countByController(c.getId()).intValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void unlinkUnit(Long buildingId, Long unitId) {
        linkUnitBuildingRepository.deleteBuildingUnit(buildingId, unitId);
    }

    @Override
    public void removeBuilding(long id) {
        Building b = em.find(Building.class, id);
        if (b == null)
            throw new NotFoundException("building");
        em.remove(b);
    }

    @Override
    public void createBuilding(CreateBuildingDto dto) {

        if(dto.getCode() == null || dto.getCode().isEmpty()) {
            throw new ValidationException("code", "null");
        }
        if(dto.getName().isEmpty()) {
            throw new ValidationException("name", "null");
        }
//        if(dto.getLatitude() == null) {
//            throw new ValidationException("latitude", "null");
//        }
//        if(dto.getLongitude() == null) {
//            throw new ValidationException("longitude", "null");
//        }

        Building building = new Building();
        building.setCode(dto.getCode());
        building.setName(dto.getName());
        building.setStreet(dto.getStreet());
        building.setCity(dto.getCity());
        building.setPostalCode(dto.getPostalCode());
        building.setNumber(dto.getNumber());
        building.setLatitude(dto.getLatitude());
        building.setLongitude(dto.getLongitude());

        buildingRepository.save(building);
    }

    @Override
    public BuildingDto modifyBuilding(long id) {
        Building b = em.find(Building.class, id);
        if(b == null)
            throw new NotFoundException("modify-building");
        return BuildingDto.make(b);
    }

    @Override
    public void acceptModifyBuilding(BuildingDto dto) {


        if(dto.getCode() == null || dto.getCode().isEmpty()) {
            throw new ValidationException("code", "Nie podano kodu");
        }
        if(dto.getName() == null || dto.getName().isEmpty()) {
            throw new ValidationException("name", "Nie podano nazwy");
        }
//        if(dto.getLatitude() == null) {
//            throw new ValidationException("latitude", "null");
//        }
//        if(dto.getLongitude() == null) {
//            throw new ValidationException("longitude", "null");
//        }

        Building b = buildingRepository.findOne(dto.getId());

        b.setCode(dto.getCode());
        b.setName(dto.getName());
        b.setStreet(dto.getStreet());
        b.setCity(dto.getCity());
        b.setPostalCode(dto.getPostalCode());
        b.setNumber(dto.getNumber());
        b.setLatitude(dto.getLatitude());
        b.setLongitude(dto.getLongitude());

        buildingRepository.save(b);
    }

    @Override
    public BuildingDetailsDto getUnitsBuildings(long id) {
        BuildingDetailsDto result;
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
                building.setLatitude(((BigDecimal) list.get(0)[3]));//.doubleValue());
                building.setLongitude(((BigDecimal) list.get(0)[4]));//.doubleValue());
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

            result = new BuildingDetailsDto();
            result.setBuilding(building);
            result.setUnits(units);
            result.setControllers(controllers);
        return result;
    }
}
