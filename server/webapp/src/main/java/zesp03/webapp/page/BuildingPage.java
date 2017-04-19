package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import zesp03.webapp.service.BuildingService;

@Controller
public class BuildingPage {
    @Autowired
    private BuildingService buildingService;

    @GetMapping("/building/{buildingId}")
    public String get(
            @PathVariable("buildingId") long buildingId,
            ModelMap model) {
        model.put("id", buildingId);
        return "building";
    }
}
