package zesp03.webapp.service;

import zesp03.webapp.dto.BuildingDetailsDto;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.UnitDto;

import java.math.BigDecimal;
import java.util.List;

public interface BuildingService {
    List<BuildingDto> getAllBuildings();
    BuildingDto getOneBuilding(long id);
    BuildingDetailsDto getDetailsOne(Long buildingId);
    void removeBuilding(long id);
    void createBuilding(String code, String name, BigDecimal latitude, BigDecimal longitude);
    BuildingDto modifyBuilding(long id);
    void acceptModify(long id, String code, String name, BigDecimal latitude, BigDecimal longitude);
    BuildingDetailsDto getUnitsBuildings(long id);
    List<UnitDto> forBuildingPage(long buildingId);
}
