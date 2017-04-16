package zesp03.webapp.service;

import zesp03.webapp.dto.CurrentDeviceStateDto;
import zesp03.webapp.dto.DeviceDetailsDto;

import java.util.List;

public interface CommonServiceWrapper {
    CurrentDeviceStateDto checkOne(Long deviceId);
    List<CurrentDeviceStateDto> checkAll();
    List<DeviceDetailsDto> checkDetailsAll();
}
