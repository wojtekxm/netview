package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.common.repository.DeviceRepository;
import zesp03.webapp.dto.CurrentDeviceStateDto;
import zesp03.webapp.dto.DeviceDetailsDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.CommonServiceWrapper;

@RestController
public class DeviceApi {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CommonServiceWrapper commonServiceWrapper;

    @GetMapping("/api/all-devices")
    public ListDto<CurrentDeviceStateDto> getAll() {
        return ListDto.make( () -> commonServiceWrapper.checkAll() );
    }

    @GetMapping("/api/device/details/all")
    public ListDto<DeviceDetailsDto> getDetailsAll() {
        return ListDto.make( () -> commonServiceWrapper.checkDetailsAll() );
    }

    @GetMapping("/api/device")
    public ContentDto<CurrentDeviceStateDto> getOne(
            @RequestParam("id") long deviceId) {
        return ContentDto.make( () -> commonServiceWrapper.checkOne(deviceId) );
    }

    @PostMapping(value = "/api/device/remove/{deviceId}")
    public BaseResultDto remove(
            @PathVariable("deviceId") long id) {
        return BaseResultDto.make( () -> deviceRepository.delete(id) );
    }
}
