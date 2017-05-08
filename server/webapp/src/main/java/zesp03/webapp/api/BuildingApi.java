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

import java.math.BigDecimal;

//TODO /api/building
@RestController
public class BuildingApi {
    @Autowired
    private BuildingService buildingService;

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/api/building/info/all")
    public ListDto<BuildingDto> getAllBuildings() {
        return ListDto.make( () -> buildingService.getAllBuildings() );
    }

    @GetMapping("/api/building/info/{buildingId}")
    public ContentDto<BuildingDto> getBuilding(
            @PathVariable("buildingId") long buildingId) {
        return ContentDto.make( () -> buildingService.getOneBuilding(buildingId) );
    }

    @GetMapping("/api/building/units/{buildingId}")
    public ListDto<UnitDto> getUnits(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make( () -> buildingService.getUnits(buildingId) );
    }

    @GetMapping("/api/building/controllers/{buildingId}")
    public ListDto<ControllerDto> getControllers(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make( () -> buildingService.getControllers(buildingId) );
    }

    @GetMapping("/api/building/devices-details/{buildingId}")
    public ListDto<DeviceDetailsDto> getDevices(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make( () -> deviceService.checkDetailsByBuilding(buildingId) );
    }

    @PostMapping("/api/building/unlink-unit")
    public BaseResultDto unlinkUnit(
            @RequestBody BuildingAndUnitDto dto) {
        return BaseResultDto.make( () ->
                buildingService.unlinkUnit(
                        dto.getBuildingId(), dto.getUnitId()
                )
        );
    }

    @GetMapping("/api/unitsbuildings")
    public ContentDto<BuildingDetailsDto> getUnitsBuildings(
            @RequestParam("id") long id ) {
        return ContentDto.make( () -> buildingService.getUnitsBuildings(id) );
    }

    @PostMapping("/api/building/remove/{buildingId}")
    public BaseResultDto removeBuilding(
            @PathVariable("buildingId") long buildingId) {
        return BaseResultDto.make( () -> buildingService.removeBuilding(buildingId) );
    }

    @PostMapping(value = "/api/building/create", consumes = "application/json")
    public BaseResultDto createBuilding(
            @RequestBody CreateBuildingDto dto) {
        return BaseResultDto.make( () -> buildingService.createBuilding(dto) );
    }

    @PostMapping(value = "/api/building/create", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto createBuilding(
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            @RequestParam(value = "street", required = false) String street,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "postalCode", required = false) String postalCode,
            @RequestParam(value = "number", required = false) String number,
            @RequestParam("latitude")  BigDecimal latitude,
            @RequestParam("longitude")  BigDecimal longitude) {
        CreateBuildingDto dto = new CreateBuildingDto();

        dto.setCode(code);
        dto.setName(name);
        dto.setStreet(street);
        dto.setCity(city);
        dto.setPostalCode(postalCode);
        dto.setNumber(number);
        dto.setLatitude(latitude);
        dto.setLongitude(longitude);

        return BaseResultDto.make( () -> buildingService.createBuilding(dto) );
    }

    @GetMapping("/api/modify-building")
    public ContentDto<BuildingDto> modifyBuilding(
            @RequestParam("id") long id) {
        return ContentDto.make( () -> buildingService.modifyBuilding(id) );
    }

    @PostMapping(value = "/api/accept-modify-building", consumes = "application/json")
    public BaseResultDto acceptModifyBuilding(
            @RequestBody BuildingDto dto) {
        return BaseResultDto.make( () -> buildingService.acceptModifyBuilding(dto) );
    }

    @PostMapping(value = "/api/accept-modify-building", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto acceptModifyBuilding(
            @RequestParam("id") long id,
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            @RequestParam("street") String street,
            @RequestParam("city") String city,
            @RequestParam("postalCode") String postalCode,
            @RequestParam("number") String number,
            @RequestParam("latitude") BigDecimal latitude,
            @RequestParam("longitude") BigDecimal longitude) {
        return BaseResultDto.make(() -> {
            BuildingDto dto = new BuildingDto();

            dto.setId(id);
            dto.setCode(code);
            dto.setName(name);
            dto.setStreet(street);
            dto.setCity(city);
            dto.setPostalCode(postalCode);
            dto.setNumber(number);
            dto.setLatitude(latitude);
            dto.setLongitude(longitude);

            buildingService.acceptModifyBuilding(dto);
        });

    }
}


