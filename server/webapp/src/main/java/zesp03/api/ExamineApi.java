package zesp03.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.App;
import zesp03.data.ExamineResult;
import zesp03.dto.ExamineResultDto;
import zesp03.exception.SNMPException;

@RestController
public class ExamineApi {
    @PostMapping(value = "/api/examine", consumes = "application/x-www-form-urlencoded")
    public ExamineResultDto examineOne(
            @RequestParam("id") long controllerId) {
        ExamineResultDto result = new ExamineResultDto(true);
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
