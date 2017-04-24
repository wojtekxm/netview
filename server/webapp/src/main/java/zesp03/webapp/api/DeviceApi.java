package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.CurrentDeviceStateDto;
import zesp03.webapp.dto.DeviceDetailsDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.DeviceService;

@RestController
public class DeviceApi {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/api/all-devices")
    public ListDto<CurrentDeviceStateDto> getAll() {
        return ListDto.make( () -> deviceService.checkAll() );
    }

    @GetMapping("/api/device/details/all")
    public ListDto<DeviceDetailsDto> getDetailsAll() {
        return ListDto.make( () -> deviceService.checkDetailsAll() );
    }

    @GetMapping("/api/device")
    public ContentDto<CurrentDeviceStateDto> getOne(
            @RequestParam("id") long deviceId) {
        return ContentDto.make( () -> deviceService.checkOne(deviceId) );
    }

    @PostMapping(value = "/api/device/remove/{deviceId}")
    public BaseResultDto remove(
            @PathVariable("deviceId") long id) {
        return BaseResultDto.make( () -> deviceService.remove(id) );
    }
}
