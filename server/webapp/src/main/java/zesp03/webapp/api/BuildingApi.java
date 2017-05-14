package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.*;
import zesp03.webapp.dto.input.BuildingAndUnitDto;
import zesp03.webapp.dto.input.CreateBuildingDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.BuildingService;
import zesp03.webapp.service.DeviceService;

@RestController
@RequestMapping("/api/building")
public class BuildingApi {
    @Autowired
    private BuildingService buildingService;

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/info/all")
    public ListDto<BuildingDto> getAllBuildings() {
        return ListDto.make( () -> buildingService.getAllBuildings() );
    }

    @GetMapping("/info/{buildingId}")
    public ContentDto<BuildingDto> getBuilding(
            @PathVariable("buildingId") long buildingId) {
        return ContentDto.make( () -> buildingService.getOneBuilding(buildingId) );
    }

    @GetMapping("/units/{buildingId}")
    public ListDto<UnitDto> getUnits(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make( () -> buildingService.getUnits(buildingId) );
    }

    @GetMapping("/controllers/{buildingId}")
    public ListDto<ControllerDto> getControllersInfo(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make( () -> buildingService.getControllersInfo(buildingId) );
    }

    @GetMapping("/controllers-details/{buildingId}")
    public ListDto<ControllerDetailsDto> getControllersDetails(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make( () -> buildingService.getControllersDetails(buildingId) );
    }

    @GetMapping("/devices-details/{buildingId}")
    public ListDto<DeviceDetailsDto> getDevices(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make( () -> deviceService.checkDetailsByBuilding(buildingId) );
    }

    @PostMapping("/unlink-unit")
    public BaseResultDto unlinkUnit(
            @RequestBody BuildingAndUnitDto dto) {
        return BaseResultDto.make( () ->
                buildingService.unlinkUnit(
                        dto.getBuildingId(), dto.getUnitId()
                )
        );
    }

    @PostMapping("/remove/{buildingId}")
    public BaseResultDto removeBuilding(
            @PathVariable("buildingId") long buildingId) {
        return BaseResultDto.make( () -> buildingService.removeBuilding(buildingId) );
    }

    @PostMapping(value = "/create", consumes = "application/json")
    public BaseResultDto createBuilding(
            @RequestBody CreateBuildingDto dto) {
        return BaseResultDto.make( () -> buildingService.createBuilding(dto) );
    }



    @PostMapping(value = "/accept-modify", consumes = "application/json")
    public BaseResultDto acceptModifyBuilding(
            @RequestBody BuildingDto dto) {
        return BaseResultDto.make( () -> buildingService.acceptModifyBuilding(dto) );
    }
}


