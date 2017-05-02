package zesp03.webapp.service;

import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.LinkUnitBuildingDto;
import zesp03.webapp.dto.UnitBuildingsDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.dto.input.CreateUnitDto;

import java.util.List;

public interface UnitService {
    List<UnitDto> getAll();
    UnitDto getOne(long unitId);
    UnitBuildingsDto getUnitBuildings(long unitId);
    UnitBuildingsDto linkUnitAllBuildings(long unitId);
    List<BuildingDto> listForLinkUnitAllBuildings(long unitId);
    List<BuildingDto> getBuildings(Long unitId);
    void create(CreateUnitDto dto);
    void removeUnit(Long unitId);
    UnitBuildingsDto getDetailsOne(Long unitId);
    UnitDto modifyUnit(long id);
    void acceptModifyUnit(UnitDto dto);
    List<LinkUnitBuildingDto> getAllLinkUnitBuildings();
    LinkUnitBuildingDto getLinkUnitBuilding(long id);
    void connect(Long buildingId, Long unitId);
    List<BuildingDto> LinkUnitBuildingPage_GET_link_unit_all_buildings(long id);
    void LinkUnitBuildingPage_GET_remove_unit_buildings(long id_unit, long id_building);
    List<UnitDto> LinkUnitBuildingPage_GET_link_building_all_units(long id);
    void LinkUnitBuildingPage_GET_link_building_units(long id_unit, long id_building);
    List<UnitDto> LinkUnitBuildingPage_GET_remove_building_all_units(long buildingId);
    void LinkUnitBuildingPage_GET_remove_buildings_units(long id_building, long id_unit);
    List<BuildingDto> UnitPage_GET_unit(long id);
}
