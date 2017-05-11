package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import zesp03.webapp.service.BuildingService;

@Controller
public class BuildingPage {
    @Autowired
    private BuildingService buildingService;


    @GetMapping("/building/{buildingId}")
    public String get(
            @PathVariable("buildingId") long buildingId,
            ModelMap model) {
        model.put("building", buildingService.getOneBuilding(buildingId));
        return "building";
    }

    @PostMapping("/building/remove/{buildingId}")
    public String postRemove(
            @PathVariable("buildingId") long buildingId) {
        buildingService.removeBuilding(buildingId);
        return "redirect:/all-buildings";
}
}