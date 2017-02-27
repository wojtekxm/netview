package zesp03.rest.resource;

import zesp03.core.App;
import zesp03.data.DeviceData;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("all-devices")
public class AllDevicesResource {
    @GET
    @Produces("application/json")
    public List<DeviceData> getAllDevices() {
        return App.checkDevices();
    }
}
