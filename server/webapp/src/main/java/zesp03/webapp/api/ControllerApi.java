package zesp03.webapp.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.ControllerDetailsDto;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.dto.DeviceDetailsDto;
import zesp03.webapp.dto.input.CreateControllerDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.ControllerService;
import zesp03.webapp.service.DeviceService;

@RestController
@RequestMapping("/api/controller")
public class ControllerApi {
    private static final Logger log = LoggerFactory.getLogger(ControllerApi.class);

    @Autowired
    private ControllerService controllerService;

    @Autowired
    private DeviceService deviceService;

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

    @GetMapping("/devices-details/{controllerId}")
    public ListDto<DeviceDetailsDto> getDevicesDetails(
            @PathVariable("controllerId") long controllerId) {
        return ListDto.make( () -> deviceService.checkDetailsByController(controllerId) );
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
        log.debug("/accept-modify-controller");
        log.debug("dto.getId()={}", dto.getId());
        log.debug("dto.getName()={}", dto.getName());
        log.debug("dto.getIpv4()={}", dto.getIpv4());
        log.debug("dto.getDescription()={}", dto.getDescription());
        log.debug("dto.getCommunityString()={}", dto.getCommunityString());
        log.debug("dto.getBuildingId()={}", dto.getBuildingId());
        log.debug("dto.isFake()={}\n", dto.isFake());
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
        log.debug("/accept-modify-controller");
        log.debug("id={}", id);
        log.debug("name={}", name);
        log.debug("ipv4={}", ipv4);
        log.debug("description={}", id);
        log.debug("communityString={}", communityString);
        log.debug("buildingId={}", buildingId);
        log.debug("fake={}\n", fake);
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
