package zesp03.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/some-device")
public class SomeDevice {
    @GET
    @Produces("application/json")
    public Integer xd() {
        return 42;
    }
}
