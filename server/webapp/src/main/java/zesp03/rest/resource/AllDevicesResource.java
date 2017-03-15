package zesp03.rest.resource;

import zesp03.dto.DeviceStateDto;
import zesp03.service.DeviceService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.stream.Collectors;

@Path("all-devices")
public class AllDevicesResource {
    @GET
    @Produces("application/json")
    public List<DeviceStateDto> getAllDevices() {
        return new DeviceService()
                .checkAll()
                .stream()
                .map( di -> {
                    DeviceStateDto dto = new DeviceStateDto();
                    dto.wrap(di);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
