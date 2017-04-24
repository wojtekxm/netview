package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.common.service.SurveySavingService;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.service.TestService;

@Deprecated
@RestController
@RequestMapping("/api/test")
public class TestApi {
    @Autowired
    private SurveySavingService surveySavingService;

    @Autowired
    private TestService testService;

    @PostMapping("/mark-test/{deviceId}/{frequencyMhz}/{validAfter}")
    public BaseResultDto postMarkTest(
            @PathVariable("deviceId") long deviceId,
            @PathVariable("frequencyMhz") int frequencyMhz,
            @PathVariable("validAfter") int validAfter) {
        return BaseResultDto.make( () -> testService.markTest(deviceId, frequencyMhz, validAfter) );
    }

    @PostMapping("/just-delete/{deviceId}/{frequencyMhz}/{validAfter}")
    public BaseResultDto postJustDelete(
            @PathVariable("deviceId") long deviceId,
            @PathVariable("frequencyMhz") int frequencyMhz,
            @PathVariable("validAfter") int validAfter) {
        return BaseResultDto.make( () -> testService.justDelete(deviceId, frequencyMhz, validAfter) );
    }

    @PostMapping("/select-and-delete/{deviceId}/{frequencyMhz}/{validAfter}")
    public BaseResultDto postSelectAndDelete(
            @PathVariable("deviceId") long deviceId,
            @PathVariable("frequencyMhz") int frequencyMhz,
            @PathVariable("validAfter") int validAfter) {
        return BaseResultDto.make( () -> testService.selectAndDelete(deviceId, frequencyMhz, validAfter) );
    }

    @GetMapping("/select-surveys/{deviceId}/{frequencyMhz}")
    public BaseResultDto getSelectSurveys(
            @PathVariable("deviceId") long deviceId,
            @PathVariable("frequencyMhz") int frequencyMhz) {
        return BaseResultDto.make( () -> testService.selectSurveys(deviceId, frequencyMhz) );
    }
}
