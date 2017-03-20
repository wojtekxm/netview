package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.data.MinmaxSurveyData;
import zesp03.common.exception.ValidationException;
import zesp03.common.service.SurveyService;
import zesp03.webapp.dto.AverageSurveyDto;
import zesp03.webapp.dto.OriginalSurveyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/surveys")
public class SurveysApi {
    private final SurveyService surveyService;

    @Autowired
    public SurveysApi(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("original")
    public List<OriginalSurveyDto> getOriginal(
            @RequestParam("device") long device,
            @RequestParam("start") int start,
            @RequestParam("end") int end) {
        if(start < 0)
            throw new ValidationException("start", "less than 0");
        if(end < 0)
            throw new ValidationException("end", "less than 0");
        if(end <= start)
            throw new ValidationException("end", "end must be after start");

        return surveyService.getOriginal(device, start, end)
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
    public AverageSurveyDto getAverage(
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
        result.setAvgClients( surveyService.getAverage(device, start, end) );
        return result;
    }

    @GetMapping("multi-average")
    public List<AverageSurveyDto> getMultiAverage(
            @RequestParam("device") long device,
            @RequestParam("start") int start,
            @RequestParam("groups") int groups,
            @RequestParam("groupTime") int groupTime) {
        if(start < 0)
            throw new ValidationException("start", "less than 0");
        if(groups < 0)
            throw new ValidationException("groups", "less than 0");
        if(groupTime < 1)
            throw new ValidationException("groupTime", "less than 1");

        List<Double> list = surveyService.getMultiAverage(device, start, groups, groupTime);
        List<AverageSurveyDto> result = new ArrayList<>();
        int begin = start;
        for(Double avg : list) {
            AverageSurveyDto dto = new AverageSurveyDto();
            dto.setDeviceId(device);
            dto.setAvgClients(avg);
            dto.setTimeStart(begin);
            dto.setTimeEnd(begin + groupTime);
            begin += groupTime;
            result.add(dto);
        }
        return result;
    }

    @GetMapping("minmax")
    public MinmaxSurveyData getMinmax(
            @RequestParam("device") long device,
            @RequestParam("start") int start,
            @RequestParam("end") int end) {
        if(start < 0)
            throw new ValidationException("start", "less than 0");
        if(end < 0)
            throw new ValidationException("end", "less than 0");
        if(end <= start)
            throw new ValidationException("end", "end must be after start");

        return surveyService.getMinmax(device, start, end);
    }

    @GetMapping("multi-minmax")
    public List<MinmaxSurveyData> getMultiMinmax(
            @RequestParam("device") long device,
            @RequestParam("start") int start,
            @RequestParam("groups") int groups,
            @RequestParam("groupTime") int groupTime) {
        if(start < 0)
            throw new ValidationException("start", "less than 0");
        if(groups < 0)
            throw new ValidationException("groups", "less than 0");
        if(groupTime < 1)
            throw new ValidationException("groupTime", "less than 1");

        return surveyService.getMultiMinmax(device, start, groups, groupTime);
    }
}
