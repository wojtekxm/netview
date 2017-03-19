package zesp03.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.data.MinmaxSurveyData;
import zesp03.dto.AverageSurveyDto;
import zesp03.dto.OriginalSurveyDto;
import zesp03.exception.ValidationException;
import zesp03.service.SurveyService;

import java.util.List;
import java.util.stream.Collectors;

@RestController("/api/surveys")
public class SurveysApi {
    @GetMapping("original")
    public List<OriginalSurveyDto> getClientsOriginal(
            @RequestParam("device") long device,
            @RequestParam("start") int start,
            @RequestParam("end") int end) {
        if(start < 0)
            throw new ValidationException("start", "less than 0");
        if(end < 0)
            throw new ValidationException("end", "less than 0");
        if(end < start)
            throw new ValidationException("end", "end is less than start");

        return new SurveyService()
                .getOriginalSurveys(device, start, end)
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
        if(end < start)
            throw new ValidationException("end", "end is less than start");

        final AverageSurveyDto result = new AverageSurveyDto();
        result.setDeviceId(device);
        result.setTimeStart(start);
        result.setTimeEnd(end);
        result.setAvgClients( new SurveyService().getAverageSurvey(device, start, end) );
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
        if(end < start)
            throw new ValidationException("end", "end is less than start");

        return new SurveyService().getMinmaxSimple(device, start, end);
    }
}
