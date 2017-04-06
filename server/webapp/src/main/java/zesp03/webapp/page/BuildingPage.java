package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.service.BuildingService;

import java.util.List;

@Controller
public class BuildingPage {
    @Autowired
    private BuildingService buildingService;

    @GetMapping("/building")
    public String get(
            @RequestParam("id") long id,
            ModelMap model) {
        BuildingDto b = buildingService.getOneBuilding(id);
        List<UnitDto> u = buildingService.forBuildingPage(b.getId());
        model.put( "building", b );
        model.put( "units", u );
        return "building";
    }
}
