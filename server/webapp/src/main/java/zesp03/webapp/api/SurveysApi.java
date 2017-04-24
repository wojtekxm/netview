package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.common.data.ShortSurvey;
import zesp03.common.data.SurveyPeriodAvgMinMax;
import zesp03.common.exception.SNMPException;
import zesp03.common.exception.ValidationException;
import zesp03.common.service.SurveyModifyingService;
import zesp03.common.service.SurveyReadingService;
import zesp03.webapp.dto.input.ImportFakeSurveysDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.ImportService;

import java.time.Instant;

@RestController
@RequestMapping("/api/surveys")
public class SurveysApi {
    @Autowired
    private SurveyReadingService surveyReadingService;

    @Autowired
    private SurveyModifyingService surveyModifyingService;

    @Autowired
    private ImportService importService;

    @PostMapping(value = "/examine/all")
    public BaseResultDto examineAll() {
        final Instant t0 = Instant.now();
        BaseResultDto result = new BaseResultDto();
        try {
            surveyModifyingService.examineAll();
        }
        catch(SNMPException exc) {
            result.setSuccess(false);
            result.setError("SNMP error");
        }
        result.makeQueryTime(t0);
        return result;
    }

    @PostMapping(value = "/examine/{id}")
    public BaseResultDto examineOne(
            @PathVariable("id") long controllerId) {
        final Instant t0 = Instant.now();
        BaseResultDto result = new BaseResultDto();
        try {
            surveyModifyingService.examineOne(controllerId);
        }
        catch(SNMPException exc) {
            result.setSuccess(false);
            result.setError("SNMP error");
        }
        result.makeQueryTime(t0);
        return result;
    }

    @PostMapping("/fake")
    public BaseResultDto fakeSurveys(@RequestBody ImportFakeSurveysDto dto) {
        return BaseResultDto.make( () -> importService.fakeSurveys(dto) );
    }

    @GetMapping("/original")
    public ListDto<ShortSurvey> getOriginal(
            @RequestParam("device") long device,
            @RequestParam("frequency") int frequencyMhz,
            @RequestParam("start") int start,
            @RequestParam("end") int end) {
        if(start < 0) {
            throw new ValidationException("start", "less than 0");
        }
        if(end <= start) {
            throw new ValidationException("end", "end must be after start");
        }

        return ListDto.make( () -> surveyReadingService.getOriginal(device, frequencyMhz, start, end) );
    }

    @GetMapping("/avg-min-max")
    public ListDto<SurveyPeriodAvgMinMax> getMultiAvgMinMax2(
            @RequestParam("device") long device,
            @RequestParam("frequency") int frequencyMhz,
            @RequestParam("start") int start,
            @RequestParam("end") int end,
            @RequestParam("groupTime") int groupTime) {
        return ListDto.make( () -> surveyReadingService.getMultiAvgMinMax(device, frequencyMhz, start, end, groupTime) );
    }
}
