package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.dto.input.CreateControllerDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.ControllerService;

@RestController
public class ControllerApi {
    @Autowired
    private ControllerService controllerService;

    @GetMapping("/api/all-controllers")
    public ListDto<ControllerDto> getAllControllers() {
        return ListDto.make( () -> controllerService.getAll() );
    }

    @GetMapping("/api/controller")
    public ContentDto<ControllerDto> getController(
            @RequestParam("id") long id) {
        return ContentDto.make( () -> controllerService.getOne(id) );
    }

    @PostMapping(value = "/api/controller/remove/{controllerId}")
    public BaseResultDto remove(
            @PathVariable("controllerId") long id) {
        return BaseResultDto.make( () -> controllerService.remove(id) );
    }

    @PostMapping(value = "/api/controller/create", consumes = "application/json")
    public BaseResultDto create(
            @RequestBody CreateControllerDto dto) {
        return BaseResultDto.make( () -> controllerService.create(dto) );
    }

    @PostMapping(value = "/api/controller/create", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto create(
            @RequestParam("name") String name,
            @RequestParam("ipv4") String ipv4,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "buildingId", required = false) Long buildingId) {
        return BaseResultDto.make( () -> {
            CreateControllerDto dto = new CreateControllerDto();
            dto.setName(name);
            dto.setIpv4(ipv4);
            dto.setDescription(description);
            dto.setBuildingId(buildingId);
            controllerService.create(dto);
        } );
    }
}
