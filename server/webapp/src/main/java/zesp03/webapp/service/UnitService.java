package zesp03.webapp.service;

import zesp03.webapp.dto.LinkUnitBuildingDto;
import zesp03.webapp.dto.UnitBuildingsDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.dto.input.CreateUserDto;

import java.util.List;

public interface UnitService {
    List<UnitDto> getAll();
    UnitBuildingsDto getOne(long id);
    void create(CreateUserDto dto);
    void remove(Long unitId);
    UnitDto modify(long id);
    void acceptModifyUnit(long id, String code, String description);
    List<LinkUnitBuildingDto> getAllLinkUnitBuildings();
    LinkUnitBuildingDto getLinkUnitBuilding(long id);
}
