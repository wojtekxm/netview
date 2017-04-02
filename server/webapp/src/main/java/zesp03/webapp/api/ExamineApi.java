package zesp03.webapp.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.exception.SNMPException;
import zesp03.common.service.SurveyService;
import zesp03.webapp.dto.ExamineResultDto;
import zesp03.webapp.dto.result.ContentDto;

import java.time.Instant;

@RestController
public class ExamineApi {
    private final SurveyService surveyService;
    private static final Logger log = LoggerFactory.getLogger(ExamineApi.class);

    @Autowired
    public ExamineApi(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping(value = "/api/examine", consumes = "application/x-www-form-urlencoded")
    public ContentDto<ExamineResultDto> examineOne(
            @RequestParam("id") long controllerId) {
        final Instant t0 = Instant.now();
        ExamineResultDto e = new ExamineResultDto();
        ContentDto<ExamineResultDto> result = new ContentDto<>();
        result.setContent(e);
        e.setControllerId(controllerId);
        try {
            e.setUpdatedDevices(surveyService.examineOne(controllerId));
        }
        catch(SNMPException exc) {
            e.setUpdatedDevices(0);
            result.setSuccess(false);
            result.setError("SNMP error");
        }
        result.makeQueryTime(t0);
        return result;
    }
}
