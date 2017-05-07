package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.ControllerDetailsDto;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.dto.input.CreateControllerDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.ControllerService;

@RestController
@RequestMapping("/api/controller")
public class ControllerApi {
    @Autowired
    private ControllerService controllerService;

    @GetMapping("/all")
    public ListDto<ControllerDto> getAll() {
        return ListDto.make( () -> controllerService.getAll() );
    }

    @GetMapping("/{controllerId}")
    public ContentDto<ControllerDto> getOne(
            @PathVariable("controllerId") long controllerId) {
        return ContentDto.make( () -> controllerService.getOne(controllerId) );
    }

    @GetMapping("/details/all")
    public ListDto<ControllerDetailsDto> getDetailsAll() {
        return ListDto.make( () -> controllerService.getDetailsAll() );
    }

    @GetMapping("/details/{controllerId}")
    public ContentDto<ControllerDetailsDto> getDetailsOne(
            @PathVariable("controllerId") long controllerId) {
        return ContentDto.make( () -> controllerService.getDetailsOne(controllerId) );
    }

    @PostMapping("/remove/{controllerId}")
    public BaseResultDto remove(
            @PathVariable("controllerId") long id) {
        return BaseResultDto.make( () -> controllerService.remove(id) );
    }

    @PostMapping(value = "/create", consumes = "application/json")
    public BaseResultDto create(
            @RequestBody CreateControllerDto dto) {
        return BaseResultDto.make( () -> controllerService.create(dto) );
    }

    @PostMapping(value = "/create", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto create(
            @RequestParam("name") String name,
            @RequestParam("ipv4") String ipv4,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "communityString", required = false) String communityString,
            @RequestParam(value = "buildingId", required = false) Long buildingId,
            @RequestParam(value = "fake", required = false) Boolean fake) {
        return BaseResultDto.make( () -> {
            CreateControllerDto dto = new CreateControllerDto();

            dto.setName(name);
            dto.setIpv4(ipv4);
            dto.setDescription(description);
            dto.setCommunityString(communityString);
            dto.setBuildingId(buildingId);
            dto.setfake(fake);
            controllerService.create(dto);
        } );
    }

    @GetMapping("/modify-controller/{controllerId}")
    public ContentDto<ControllerDto> modifyController(
            @RequestParam("id") long controllerId) {
        return ContentDto.make( () -> controllerService.getOne(controllerId) );
    }

    @PostMapping(value = "/accept-modify-controller", consumes = "application/json")
    public BaseResultDto acceptModifyController(
            @RequestBody ControllerDto dto) {
        return BaseResultDto.make( () -> controllerService.acceptModifyController(dto) );
    }

    @PostMapping(value = "/accept-modify-controller", consumes = "application/x-www-form-urlencoded")
    public BaseResultDto acceptModifyController(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("ipv4") String ipv4,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "communityString", required = false) String communityString,
            @RequestParam(value = "buildingId", required = false) Long buildingId,
            @RequestParam(value = "fake", required = false) Boolean fake) {
        return BaseResultDto.make( () -> {
            ControllerDto dto = new ControllerDto();
            dto.setId(id);
            dto.setName(name);
            dto.setIpv4(ipv4);
            dto.setDescription(description);
            dto.setCommunityString(communityString);
            dto.setBuildingId(buildingId);
              dto.setFake(fake);
            controllerService.acceptModifyController(dto);
        } );
    }


}
