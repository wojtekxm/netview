package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.data.MinmaxSurveyData;
import zesp03.common.exception.ValidationException;
import zesp03.common.service.SurveyService;
import zesp03.webapp.dto.AverageSurveyDto;
import zesp03.webapp.dto.OriginalSurveyDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController("/api/surveys")
public class SurveysApi {
    private final SurveyService surveyService;

    @Autowired
    public SurveysApi(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("original")
    public List<OriginalSurveyDto> getClientsOriginal(
            @RequestParam("device") long device,
            @RequestParam("start") int start,
            @RequestParam("end") int end) {
        if(start < 0)
            throw new ValidationException("start", "less than 0");
        if(end < 0)
            throw new ValidationException("end", "less than 0");
        if(end <= start)
            throw new ValidationException("end", "end must be after start");

        return surveyService.getOriginalSurveys(device, start, end)
                .stream()
                .map( ds -> {
                    OriginalSurveyDto dto = new OriginalSurveyDto();
                    dto.setDeviceId(device);
                    dto.setClientsSum(ds.getClientsSum());
                    dto.setTime(ds.getTimestamp());
                    return dto;
                } )
                .collect(Collectors.toList());
    }

    @GetMapping("average")
    public AverageSurveyDto getAverageSurvey(
            @RequestParam("device") long device,
            @RequestParam("start") int start,
            @RequestParam("end") int end) {
        if(start < 0)
            throw new ValidationException("start", "less than 0");
        if(end < 0)
            throw new ValidationException("end", "less than 0");
        if(end <= start)
            throw new ValidationException("end", "end must be after start");

        final AverageSurveyDto result = new AverageSurveyDto();
        result.setDeviceId(device);
        result.setTimeStart(start);
        result.setTimeEnd(end);
        result.setAvgClients( surveyService.getAverageSurvey(device, start, end) );
        return result;
    }

    @GetMapping("minmax")
    public MinmaxSurveyData getMinmaxSimple(
            @RequestParam("device") long device,
            @RequestParam("start") int start,
            @RequestParam("end") int end) {
        if(start < 0)
            throw new ValidationException("start", "less than 0");
        if(end < 0)
            throw new ValidationException("end", "less than 0");
        if(end <= start)
            throw new ValidationException("end", "end must be after start");

        return surveyService.getMinmaxSimple(device, start, end);
    }
}
