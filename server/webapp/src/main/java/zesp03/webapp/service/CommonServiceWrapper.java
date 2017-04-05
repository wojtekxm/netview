package zesp03.webapp.service;

import zesp03.webapp.dto.CurrentDeviceStateDto;

import java.util.List;

public interface CommonServiceWrapper {
    CurrentDeviceStateDto checkOneDevice(Long deviceId);
    List<CurrentDeviceStateDto> checkAllDevices();
}
