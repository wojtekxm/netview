package zesp03.webapp.service;

import zesp03.webapp.dto.BuildingDetailsDto;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.dto.input.CreateBuildingDto;

import java.math.BigDecimal;
import java.util.List;

public interface BuildingService {
    List<BuildingDto> getAllBuildings();
    BuildingDto getOneBuilding(long id);
    BuildingDetailsDto getDetailsOne(Long buildingId);
    List<UnitDto> getUnits(Long buildingId);
    void unlinkUnit(Long buildingId, Long unitId);
    void removeBuilding(long id);
    void createBuilding(CreateBuildingDto dto);
    BuildingDto modifyBuilding(long id);
    void  acceptModifyBuilding(BuildingDto dto);
    BuildingDetailsDto getUnitsBuildings(long id);
}
