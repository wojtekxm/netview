package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.common.data.ShortSurvey;
import zesp03.common.data.SurveyPeriodAvg;
import zesp03.common.data.SurveyPeriodAvgMinMax;
import zesp03.common.data.SurveyPeriodMinMax;
import zesp03.common.exception.ValidationException;
import zesp03.common.service.HistoricalSurveyService;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;

@RestController
@RequestMapping("/api/surveys")
public class SurveysApi {
    @Autowired
    private HistoricalSurveyService historicalSurveyService;
    
    @GetMapping("original")
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

        return ListDto.make( () -> historicalSurveyService.getOriginal(device, frequencyMhz, start, end) );
    }

    // zwrócony content może być null
    @GetMapping("average")
    public ContentDto<SurveyPeriodAvg> getAverage(
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

        return ContentDto.make( () -> historicalSurveyService.getAverage(device, frequencyMhz, start, end) );
    }

    @GetMapping("multi-average")
    public ListDto<SurveyPeriodAvg> getMultiAverage(
            @RequestParam("device") long device,
            @RequestParam("frequency") int frequencyMhz,
            @RequestParam("start") int start,
            @RequestParam("end") int end,
            @RequestParam("groupTime") int groupTime) {
        if(start < 0) {
            throw new ValidationException("start", "less than 0");
        }
        if(end <= start) {
            throw new ValidationException("end", "end must be after start");
        }
        if(groupTime < 1) {
            throw new ValidationException("groupTime", "less than 1");
        }

        return ListDto.make( () -> historicalSurveyService.getMultiAverage(device, frequencyMhz, start, end, groupTime) );
    }

    @GetMapping("minmax")
    public ContentDto<SurveyPeriodMinMax> getMinMax(
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

        return ContentDto.make( () -> historicalSurveyService.getMinMax(device, frequencyMhz, start, end) );
    }

    @GetMapping("multi-minmax")
    public ListDto<SurveyPeriodMinMax> getMultiMinMax(
            @RequestParam("device") long device,
            @RequestParam("frequency") int frequencyMhz,
            @RequestParam("start") int start,
            @RequestParam("end") int end,
            @RequestParam("groupTime") int groupTime) {
        if(start < 0) {
            throw new ValidationException("start", "less than 0");
        }
        if(end <= start) {
            throw new ValidationException("end", "end must be after start");
        }
        if(groupTime < 1) {
            throw new ValidationException("groupTime", "less than 1");
        }

        return ListDto.make( () -> historicalSurveyService.getMultiMinMax(device, frequencyMhz, start, end, groupTime) );
    }

    @GetMapping("multi-avg-minmax-slow")
    public ListDto<SurveyPeriodAvgMinMax> getMultiAvgMinMax(
            @RequestParam("device") long device,
            @RequestParam("frequency") int frequencyMhz,
            @RequestParam("start") int start,
            @RequestParam("end") int end,
            @RequestParam("groupTime") int groupTime) {
        return ListDto.make( () -> historicalSurveyService.getMultiAvgMinMax_Slow(device, frequencyMhz, start, end, groupTime) );
    }

    @GetMapping("multi-avg-minmax")
    public ListDto<SurveyPeriodAvgMinMax> getMultiAvgMinMax2(
            @RequestParam("device") long device,
            @RequestParam("frequency") int frequencyMhz,
            @RequestParam("start") int start,
            @RequestParam("end") int end,
            @RequestParam("groupTime") int groupTime) {
        return ListDto.make( () -> historicalSurveyService.getMultiAvgMinMax(device, frequencyMhz, start, end, groupTime) );
    }
}
