package zesp03.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.data.DeviceNow;
import zesp03.dto.DeviceStateDto;
import zesp03.service.SurveyService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DeviceApi {
    @GetMapping("/api/all-devices")
    public List<DeviceStateDto> getAll() {
        return new SurveyService()
                .checkAll()
                .stream()
                .map( dn -> {
                    DeviceStateDto dto = new DeviceStateDto();
                    dto.wrap(dn);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/api/device")
    public DeviceStateDto getOne(
            @RequestParam("id") long device) {
        DeviceNow dn = new SurveyService().checkOne(device);
        DeviceStateDto dto = new DeviceStateDto();
        dto.wrap(dn);
        return dto;
    }
}
