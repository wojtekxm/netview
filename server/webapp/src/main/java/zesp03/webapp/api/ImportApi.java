package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zesp03.webapp.dto.input.ImportFakeSurveysDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.service.ImportService;

@RestController
@RequestMapping("/api/import")
public class ImportApi {
    @Autowired
    private ImportService importService;

    @PostMapping("/fake-surveys")
    public BaseResultDto fakeSurveys(@RequestBody ImportFakeSurveysDto dto) {
        return BaseResultDto.make( () -> importService.fakeSurveys(dto) );
    }
}
