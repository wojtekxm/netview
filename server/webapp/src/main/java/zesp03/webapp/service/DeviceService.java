package zesp03.webapp.service;

import zesp03.common.entity.Device;
import zesp03.webapp.dto.CurrentDeviceStateDto;
import zesp03.webapp.dto.DeviceDetailsDto;
import zesp03.webapp.dto.DeviceDto;

import java.util.List;

public interface DeviceService {
    Device findOneNotDeletedOrThrow(Long deviceId);
    DeviceDto getOne(Long deviceId);
    List<DeviceDto> getAll();
    CurrentDeviceStateDto checkOne(Long deviceId);
    List<CurrentDeviceStateDto> checkAll();
    List<DeviceDetailsDto> checkDetailsAll();
    Long countSurveys(Long deviceId);
    Long countSurveysBefore(Long deviceId, int before);
    void remove(Long deviceId);
}
