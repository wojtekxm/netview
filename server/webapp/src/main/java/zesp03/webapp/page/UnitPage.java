package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.service.UnitService;

import java.util.List;

@Controller
public class UnitPage {
    @Autowired
    private UnitService unitService;

    @GetMapping("/unit/{unitId}")
    public String get(
            @PathVariable("unitId") long unitId,
            ModelMap model) {
        model.put("id", unitId);
        return "unit";
    }

    @PostMapping("/unit/remove/{unitId}")
    public String postRemove(
            @PathVariable("unitId") long unitId,
            ModelMap model) {
        unitService.removeUnit(unitId);
        return "redirect:/all-units";
}
    }
