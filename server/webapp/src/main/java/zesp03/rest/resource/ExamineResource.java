package zesp03.rest.resource;

import zesp03.common.App;
import zesp03.common.SNMPException;
import zesp03.dto.ExamineResultDto;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.time.Duration;
import java.time.Instant;

@Path("examine")
public class ExamineResource {
    @POST
    @Produces("application/json")
    public ExamineResultDto post(@QueryParam("id") long controllerId) {
        ExamineResultDto result = new ExamineResultDto();
        try {
            final Instant start = Instant.now();
            int updated = App.examineOne(controllerId);
            final Instant end = Instant.now();
            result.setControllerId(controllerId);
            result.setUpdatedDevices(updated);
            result.setTimeElapsed( Duration.between(start, end).toNanos() * 0.000000001 );
        }
        catch(SNMPException exc) {
            result.setSuccess(false);
            result.setError("SNMP error");
        }
        return result;
    }
}
