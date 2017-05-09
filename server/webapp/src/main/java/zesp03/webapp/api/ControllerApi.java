package zesp03.webapp.api;

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

    @PostMapping(value = "/accept-modify-controller", consumes = "application/json")
    public BaseResultDto acceptModifyController(
            @RequestBody ControllerDto dto) {
        return BaseResultDto.make( () -> controllerService.acceptModifyController(dto) );
    }



}
