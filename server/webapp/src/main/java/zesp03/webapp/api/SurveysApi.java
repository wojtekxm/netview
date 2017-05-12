package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.common.data.RangeSamples;
import zesp03.common.data.SampleAvgMinMax;
import zesp03.common.exception.SNMPException;
import zesp03.common.exception.ValidationException;
import zesp03.common.repository.DeviceSurveyRepository;
import zesp03.common.service.ExamineService;
import zesp03.common.service.SurveyModifyingService;
import zesp03.common.service.SurveyReadingService;
import zesp03.webapp.dto.input.ImportFakeSurveysDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.DeviceService;
import zesp03.webapp.service.ImportService;

import java.time.Instant;

@RestController
@RequestMapping("/api/surveys")
public class SurveysApi {
    @Autowired
    private SurveyReadingService surveyReadingService;

    @Autowired
    private ExamineService examineService;

    @Autowired
    private SurveyModifyingService surveyModifyingService;

    @Autowired
    private ImportService importService;

    @Autowired
    private DeviceSurveyRepository deviceSurveyRepository;

    @Autowired
    private DeviceService deviceService;

    @PostMapping(value = "/examine/all")
    public BaseResultDto examineAll() {
        final Instant t0 = Instant.now();
        BaseResultDto result = new BaseResultDto();
        try {
            examineService.examineAll();
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
            examineService.examineOne(controllerId);
        }
        catch(SNMPException exc) {
            result.setSuccess(false);
            result.setError("SNMP error");
        }
        result.makeQueryTime(t0);
        return result;
    }

    @PostMapping("/delete/all/all")
    public BaseResultDto deleteForAll() {
        return BaseResultDto.make( () -> surveyModifyingService.deleteForAll() );
    }

    @PostMapping("/delete/all/{before}")
    public BaseResultDto deleteForAll(
            @PathVariable("before") int before) {
        return BaseResultDto.make( () -> surveyModifyingService.deleteForAll(before) );
    }

    @PostMapping("/delete/{deviceId}/all")
    public BaseResultDto deleteForOne(
            @PathVariable("deviceId") long deviceId) {
        return BaseResultDto.make( () -> surveyModifyingService.deleteForOne(deviceId) );
    }

    @PostMapping("/delete/{deviceId}/{before}")
    public BaseResultDto deleteForOne(
            @PathVariable("deviceId") long deviceId,
            @PathVariable("before") int before) {
        return BaseResultDto.make( () -> surveyModifyingService.deleteForOne(deviceId, before) );
    }

    @PostMapping("/fake")
    public BaseResultDto fakeSurveys(@RequestBody ImportFakeSurveysDto dto) {
        return BaseResultDto.make( () -> importService.fakeSurveys(dto) );
    }

    @GetMapping("/total/all/all")
    public ContentDto<Long> totalAll() {
        return ContentDto.make( () -> deviceSurveyRepository.countNotDeleted() );
    }

    @GetMapping("/total/all/{before}")
    public ContentDto<Long> totalAllBefore(
            @PathVariable("before") int before) {
        return ContentDto.make( () -> deviceSurveyRepository.countBeforeNotDeleted(before) );
    }

    @GetMapping("/total/{deviceId}/all")
    public ContentDto<Long> totalDevice(
            @PathVariable("deviceId") long deviceId) {
        return ContentDto.make( () -> deviceService.countSurveys(deviceId) );
    }

    @GetMapping("/total/{deviceId}/{before}")
    public ContentDto<Long> totalDeviceBefore(
            @PathVariable("deviceId") long deviceId,
            @PathVariable("before") int before) {
        return ContentDto.make( () -> deviceService.countSurveysBefore(deviceId, before) );
    }

    @GetMapping("/original")
    public ContentDto< RangeSamples > getOriginal(
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

        return ContentDto.make( () -> surveyReadingService.getOriginal(device, frequencyMhz, start, end) );
    }

    @GetMapping("/avg-min-max")
    public ListDto<SampleAvgMinMax> getMultiAvgMinMax2(
            @RequestParam("device") long device,
            @RequestParam("frequency") int frequencyMhz,
            @RequestParam("start") int start,
            @RequestParam("end") int end,
            @RequestParam("groupTime") int groupTime) {
        return ListDto.make( () -> surveyReadingService.getMultiAvgMinMax(device, frequencyMhz, start, end, groupTime) );
    }
}
