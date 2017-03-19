package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.data.ExamineResult;
import zesp03.common.exception.SNMPException;
import zesp03.common.service.SurveyService;
import zesp03.webapp.dto.ExamineResultDto;

@RestController
public class ExamineApi {
    private final SurveyService surveyService;

    @Autowired
    public ExamineApi(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping(value = "/api/examine", consumes = "application/x-www-form-urlencoded")
    public ExamineResultDto examineOne(
            @RequestParam("id") long controllerId) {
        ExamineResultDto result = new ExamineResultDto(true);
        try {
            ExamineResult er = surveyService.examineOne(controllerId);
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
