package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.common.repository.DeviceRepository;
import zesp03.webapp.dto.CurrentDeviceStateDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.CommonServiceWrapper;

import java.time.Instant;

@RestController
public class DeviceApi {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CommonServiceWrapper commonServiceWrapper;

    @GetMapping("/api/all-devices")
    public ListDto<CurrentDeviceStateDto> getAll() {
        return ListDto.make( () -> commonServiceWrapper.checkAllDevices() );
    }

    @GetMapping("/api/device")
    public ContentDto<CurrentDeviceStateDto> getOne(
            @RequestParam("id") long deviceId) {
        return ContentDto.make( () -> commonServiceWrapper.checkOneDevice(deviceId) );
    }

    @PostMapping(value = "/api/device/remove/{id}")
    public BaseResultDto remove(
            @PathVariable("id") long id) {
        final Instant t0 = Instant.now();
        BaseResultDto r = new BaseResultDto();
        deviceRepository.delete(id);
        r.makeQueryTime(t0);
        return r;
    }
}
