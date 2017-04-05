package zesp03.webapp.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.exception.SNMPException;
import zesp03.common.service.SurveySavingService;
import zesp03.webapp.dto.result.BaseResultDto;

import java.time.Instant;

@RestController
public class ExamineApi {
    private static final Logger log = LoggerFactory.getLogger(ExamineApi.class);

    @Autowired
    private SurveySavingService surveySavingService;

    @PostMapping(value = "/api/examine-all")
    public BaseResultDto examineAll() {
        final Instant t0 = Instant.now();
        BaseResultDto result = new BaseResultDto();
        try {
            surveySavingService.examineAll();
        }
        catch(SNMPException exc) {
            result.setSuccess(false);
            result.setError("SNMP error");
        }
        result.makeQueryTime(t0);
        return result;
    }

    @PostMapping(value = "/api/examine/{id}")
    public BaseResultDto examineOne(
            @PathVariable("id") long controllerId) {
        final Instant t0 = Instant.now();
        BaseResultDto result = new BaseResultDto();
        try {
            surveySavingService.examineOne(controllerId);
        }
        catch(SNMPException exc) {
            result.setSuccess(false);
            result.setError("SNMP error");
        }
        result.makeQueryTime(t0);
        return result;
    }
}
