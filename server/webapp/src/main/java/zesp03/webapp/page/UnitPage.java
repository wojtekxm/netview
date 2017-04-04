package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.service.UnitService;

@Controller
public class UnitPage {
    @Autowired
    private UnitService unitService;

    @GetMapping("/unit")
    public String get(
            @RequestParam("id") long id,
            ModelMap model) {
        UnitDto dto = unitService.modify(id);
        model.put("unit", dto);
        return "unit";
    }
}
