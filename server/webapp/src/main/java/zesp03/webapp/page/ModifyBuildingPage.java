package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.service.BuildingService;

@Controller
public class ModifyBuildingPage {
    @Autowired
    private BuildingService buildingService;

    @GetMapping("/modify-building")
    public String get(
            @RequestParam("id") long id,
            ModelMap model) {
        BuildingDto dto = buildingService.modifyBuilding(id);
        model.put("building", dto);
        return "modify-building";
    }
}