package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zesp03.webapp.service.ChartService;

import java.util.List;

@Deprecated
@RestController
public class ChartApi {
    @Autowired
    private ChartService chartService;

    @GetMapping("/api/chart")
    public List<Object[]> getDev(
            @RequestParam("id") long id) {
        return chartService.getDev(id);
    }
}
