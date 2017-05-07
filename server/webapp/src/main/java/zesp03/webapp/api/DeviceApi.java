package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import zesp03.webapp.dto.DeviceDetailsDto;
import zesp03.webapp.dto.DeviceNowDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.DeviceService;

@RestController
public class DeviceApi {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/api/device/info/all")
    public ListDto<DeviceNowDto> getAll() {
        return ListDto.make( () -> deviceService.checkAll() );
    }

    @GetMapping("/api/device/details/all")
    public ListDto<DeviceDetailsDto> getDetailsAll() {
        return ListDto.make( () -> deviceService.checkDetailsAll() );
    }

    @GetMapping("/api/device/info/{deviceId}")
    public ContentDto<DeviceNowDto> getOne(
            @PathVariable("deviceId") long deviceId) {
        return ContentDto.make( () -> deviceService.checkOne(deviceId) );
    }

    @GetMapping("/api/device/details/{deviceId}")
    public ContentDto<DeviceDetailsDto> getDetailsOne(
            @PathVariable("deviceId") long deviceId) {
        return ContentDto.make( () -> deviceService.checkDetailsOne(deviceId) );
    }

    @PostMapping(value = "/api/device/remove/{deviceId}")
    public BaseResultDto remove(
            @PathVariable("deviceId") long deviceId) {
        return BaseResultDto.make( () -> deviceService.remove(deviceId) );
    }

    @PostMapping("/api/device/link-controller/{deviceId}/{controllerId}")
    public BaseResultDto linkController(
            @PathVariable("deviceId") long deviceId,
            @PathVariable("controllerId") long controllerId) {
        return BaseResultDto.make( () -> deviceService.linkController(deviceId, controllerId) );
    }

    @PostMapping("/api/device/link-building/{deviceId}/{buildingId}")
    public BaseResultDto linkBuilding(
            @PathVariable("deviceId") long deviceId,
            @PathVariable("buildingId") long buildingId) {
        return BaseResultDto.make( () -> deviceService.linkBuilding(deviceId, buildingId) );
    }

    @PostMapping("/api/device/unlink-controller/{deviceId}")
    public BaseResultDto unlinkController(
            @PathVariable("deviceId") long deviceId) {
        return BaseResultDto.make( () -> deviceService.unlinkController(deviceId) );
    }

    @PostMapping("/api/device/unlink-building/{deviceId}")
    public BaseResultDto unlinkBuilding(
            @PathVariable("deviceId") long deviceId) {
        return BaseResultDto.make( () -> deviceService.unlinkBuilding(deviceId) );
    }
}
