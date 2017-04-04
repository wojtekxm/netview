package zesp03.webapp.service;

import zesp03.webapp.dto.DeviceDto;

import java.util.List;

public interface DeviceService {
    DeviceDto getOne(Long deviceId);
    List<DeviceDto> getAll();
    void remove(Long deviceId);
}
