package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.DeviceDetailsDto;
import zesp03.webapp.dto.DeviceNowDto;
import zesp03.webapp.dto.input.LinkBuildingManyDevicesDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.DeviceService;

@RestController
@RequestMapping("/api/device")
public class DeviceApi {
    @Autowired
    private DeviceService deviceService;


    @GetMapping("/info/all")
    public ListDto<DeviceNowDto> getAll() {
        return ListDto.make( () -> deviceService.checkAll() );
    }

    @GetMapping("/details/all")
    public ListDto<DeviceDetailsDto> getDetailsAll() {
        return ListDto.make( () -> deviceService.checkDetailsAll() );
    }

    @GetMapping("/details/all-not-in-building/{buildingId}")
    public ListDto<DeviceDetailsDto> getDetailsAllNotInBuilding(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make(() -> deviceService.checkDetailsAllNotInBuilding(buildingId));
    }

    @GetMapping("/info/{deviceId}")
    public ContentDto<DeviceNowDto> getOne(
            @PathVariable("deviceId") long deviceId) {
        return ContentDto.make( () -> deviceService.checkOne(deviceId) );
    }

    @GetMapping("/details/{deviceId}")
    public ContentDto<DeviceDetailsDto> getDetailsOne(
            @PathVariable("deviceId") long deviceId) {
        return ContentDto.make( () -> deviceService.checkDetailsOne(deviceId) );
    }

    @PostMapping("/remove/{deviceId}")
    public BaseResultDto remove(
            @PathVariable("deviceId") long deviceId) {
        return BaseResultDto.make( () -> deviceService.remove(deviceId) );
    }

    @PostMapping("/link-controller/{deviceId}/{controllerId}")
    public BaseResultDto linkController(
            @PathVariable("deviceId") long deviceId,
            @PathVariable("controllerId") long controllerId) {
        return BaseResultDto.make( () -> deviceService.linkController(deviceId, controllerId) );
    }

    @PostMapping("/link-building/{deviceId}/{buildingId}")
    public BaseResultDto linkBuilding(
            @PathVariable("deviceId") long deviceId,
            @PathVariable("buildingId") long buildingId) {
        return BaseResultDto.make( () -> deviceService.linkBuilding(deviceId, buildingId) );
    }

    @PostMapping("/link-building")
    public BaseResultDto linkBuildingMany(@RequestBody LinkBuildingManyDevicesDto dto) {
        return BaseResultDto.make( () -> deviceService.linkBuilding(dto) );
    }

    @PostMapping("/unlink-controller/{deviceId}")
    public BaseResultDto unlinkController(
            @PathVariable("deviceId") long deviceId) {
        return BaseResultDto.make( () -> deviceService.unlinkController(deviceId) );
    }

    @PostMapping("/unlink-building/{deviceId}")
    public BaseResultDto unlinkBuilding(
            @PathVariable("deviceId") long deviceId) {
        return BaseResultDto.make( () -> deviceService.unlinkBuilding(deviceId) );
    }
}
