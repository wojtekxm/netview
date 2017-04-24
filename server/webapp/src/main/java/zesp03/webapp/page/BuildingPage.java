package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BuildingPage {
    @GetMapping("/building/{buildingId}")
    public String get(
            @PathVariable("buildingId") long buildingId,
            ModelMap model) {
        model.put("id", buildingId);
        return "building";
    }
}
