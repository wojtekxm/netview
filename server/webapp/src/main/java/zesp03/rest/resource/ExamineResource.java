package zesp03.rest.resource;

import zesp03.common.App;
import zesp03.data.ExamineResult;
import zesp03.dto.ExamineResultDto;
import zesp03.exception.SNMPException;

import javax.ws.rs.*;

@Path("examine")
public class ExamineResource {
    @POST
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public ExamineResultDto post(@FormParam("id") long controllerId) {
        ExamineResultDto result = new ExamineResultDto();
        try {
            ExamineResult er = App.examineOne(controllerId);
            result.setControllerId(controllerId);
            result.setUpdatedDevices(er.getUpdatedDevices());
            result.setTimeElapsed( er.getSeconds() );
        }
        catch(SNMPException exc) {
            result.setSuccess(false);
            result.setError("SNMP error");
        }
        return result;
    }
}
