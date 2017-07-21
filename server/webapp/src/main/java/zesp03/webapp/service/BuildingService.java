/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.service;

import zesp03.webapp.dto.*;
import zesp03.webapp.dto.input.CreateBuildingDto;

import java.util.List;

public interface BuildingService {
    List<BuildingDto> getAllBuildings();
    BuildingDto getOneBuilding(long id);
    BuildingDetailsDto getDetailsOne(Long buildingId);
    List<UnitDto> getUnits(Long buildingId);
    List<ControllerDto> getControllersInfo(Long buildingId);
    List<ControllerDetailsDto> getControllersDetails(Long buildingId);
    void unlinkUnit(Long buildingId, Long unitId);
    void removeBuilding(long id);
    void createBuilding(CreateBuildingDto dto);
    BuildingDto modifyBuilding(long id);
    void  acceptModifyBuilding(BuildingDto dto);
    BuildingDetailsDto getUnitsBuildings(long id);
}
