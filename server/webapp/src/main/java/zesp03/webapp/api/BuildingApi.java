package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.BuildingUnitsControllersDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.BuildingService;

import java.math.BigDecimal;

@RestController
public class BuildingApi {
    @Autowired
    private BuildingService buildingService;

    @GetMapping("/api/all-buildings")
    public ListDto<BuildingDto> getAllBuildings() {
        return ListDto.make( () -> buildingService.getAllBuildings() );
    }

    @GetMapping("/api/building")
    public ContentDto<BuildingDto> getBuilding(
            @RequestParam("id") long id) {
        return ContentDto.make( () -> buildingService.getOneBuilding(id) );
    }

    @GetMapping("/api/unitsbuildings")
    public ContentDto<BuildingUnitsControllersDto> getUnitsBuildings(
            @RequestParam("id") long id ) {
        return ContentDto.make( () -> buildingService.getUnitsBuildings(id) );
    }

    @PostMapping(value = "/api/remove-building", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto remove(
            @RequestParam("id") long id) {
        return BaseResultDto.make( () -> buildingService.removeBuilding(id) );
    }

    @PostMapping(value = "/api/create-building", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto create(
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            @RequestParam("latitude")  BigDecimal latitude,
            @RequestParam("longitude")  BigDecimal longitude) {
        return BaseResultDto.make( () -> buildingService.createBuilding(code, name, latitude, longitude) );
    }

    @GetMapping("/api/modify-building")
    public ContentDto<BuildingDto> modify(
            @RequestParam("id") long id) {
        return ContentDto.make( () -> buildingService.modifyBuilding(id) );
    }

    @PostMapping(value = "/api/accept-modify-building", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto acceptModify(
            @RequestParam("id") long id,
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            @RequestParam("latitude")  BigDecimal latitude,
            @RequestParam("longitude")  BigDecimal longitude) {
        return BaseResultDto.make( () -> buildingService.acceptModify(id, code, name, latitude, longitude) );
    }
}


