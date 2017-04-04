package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.LinkUnitBuildingDto;
import zesp03.webapp.dto.UnitBuildingsDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.dto.input.CreateUserDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.UnitService;

@RestController
public class UnitApi {
    @Autowired
    private UnitService unitService;

    @GetMapping("/api/all-units")
    public ListDto<UnitDto> getAllUnits() {
        return ListDto.make( () -> unitService.getAll() );
    }

    @GetMapping("/api/unit")
    public ContentDto<UnitBuildingsDto> getUnit(
            @RequestParam("id") long id ) {
        return ContentDto.make( () -> unitService.getOne(id) );
    }

    @GetMapping("/api/link-unit-building")
    public ContentDto<LinkUnitBuildingDto> getLinkUnitBuilding(
            @RequestParam("id") long id) {
        return ContentDto.make( () -> unitService.getLinkUnitBuilding(id) );
    }

    @GetMapping("/api/all-link-units-buildings")
    public ListDto<LinkUnitBuildingDto> getAllLinkUnitsBuildings() {
        return ListDto.make( () -> unitService.getAllLinkUnitBuildings() );
    }

    @PostMapping(value = "/api/unit/create", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto create(
            @RequestParam("code") String code,
            @RequestParam(value = "description", required = false) String description) {
        CreateUserDto dto = new CreateUserDto();
        dto.setCode(code);
        dto.setDescription(description);
        return BaseResultDto.make( () -> unitService.create(dto) );
    }

    @PostMapping(value = "/api/unit/create", consumes = "application/json")
    public BaseResultDto create(
            @RequestBody CreateUserDto dto) {
        return BaseResultDto.make( () -> unitService.create(dto) );
    }

    @PostMapping(value = "/api/unit/remove/{unitId}")
    public BaseResultDto remove(
            @PathVariable("unitId") long id) {
        return BaseResultDto.make( () -> unitService.remove(id) );
    }

    @GetMapping("/api/modify-unit")
    public ContentDto<UnitDto> modify(
            @RequestParam("id") long id) {
        return ContentDto.make( () -> unitService.modify(id) );
    }

    @PostMapping(value = "/api/accept-modify-unit", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto acceptModifyUnit(
            @RequestParam("id") long id,
            @RequestParam("code") String code,
            @RequestParam("description") String description) {
        return BaseResultDto.make( () -> unitService.acceptModifyUnit(id, code, description) );
    }
}
