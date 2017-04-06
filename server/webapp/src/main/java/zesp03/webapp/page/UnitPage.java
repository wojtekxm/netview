package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.service.UnitService;

import java.util.List;

@Controller
public class UnitPage {
    @Autowired
    private UnitService unitService;

    @GetMapping("/unit")
    public String get(
            @RequestParam("id") long id,
            ModelMap model) {
        UnitDto u = unitService.getOne(id);
        List<BuildingDto> b = unitService.UnitPage_GET_unit(id);
        model.put( "unit", u );
        model.put( "buildings", b );
        return "unit";
    }
}
