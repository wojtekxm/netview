package zesp03.rest;

import zesp03.data.SurveyRow;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/some-device")
public class SomeDevice {
    @GET
    @Produces("application/json")
    public SurveyRow xd() {
        SurveyRow sr = new SurveyRow();
        sr.setId(23456);
        sr.setEnabled(true);
        sr.setClientsSum(50);
        sr.setTimestamp(1400100100);
        sr.setDeviceId(2);
        return sr;
    }
}
