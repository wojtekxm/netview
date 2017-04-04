package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import zesp03.webapp.dto.LinkUnitBuildingDto;
import zesp03.webapp.service.UnitService;

import java.util.List;

@Controller
public class LinkUnitBuildingPage {
    @Autowired
    private UnitService unitService;

    @GetMapping("/unitsbuildings")
    public String get(ModelMap model) {
        List<LinkUnitBuildingDto> list = unitService.getAllLinkUnitBuildings();
        model.put("list", list);
        return "link-unit-building";
    }
}
