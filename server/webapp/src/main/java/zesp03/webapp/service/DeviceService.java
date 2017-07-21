/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.service;

import zesp03.common.entity.Device;
import zesp03.webapp.dto.DeviceDetailsDto;
import zesp03.webapp.dto.DeviceDto;
import zesp03.webapp.dto.DeviceNowDto;
import zesp03.webapp.dto.input.LinkBuildingManyDevicesDto;

import java.util.List;

public interface DeviceService {
    Device findOneNotDeletedOrThrow(Long deviceId);
    DeviceDto getOne(Long deviceId);
    List<DeviceDto> getAll();
    List<DeviceNowDto> checkAll();
    List<DeviceDetailsDto> checkDetailsAll();
    List<DeviceDetailsDto> checkDetailsAllNotInBuilding(Long buildingId);
    List<DeviceDetailsDto> checkDetailsByController(Long controllerId);
    List<DeviceDetailsDto> checkDetailsByBuilding(Long buildingId);
    DeviceNowDto checkOne(Long deviceId);
    DeviceDetailsDto checkDetailsOne(Long deviceId);
    void remove(Long deviceId);
    void linkController(Long deviceId, Long controllerId);
    void linkBuilding(Long deviceId, Long buildingId);
    void linkBuilding(LinkBuildingManyDevicesDto dto);
    void unlinkController(Long deviceId);
    void unlinkBuilding(Long deviceId);
}
