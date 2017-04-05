package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.service.ControllerService;

import java.util.List;

@Controller
public class AllControllersPage {
    @Autowired
    private ControllerService controllerService;

    @GetMapping("/all-controllers")
    public String get(ModelMap model) {
        List<ControllerDto> list = controllerService.getAll();
        model.put("list", list);
        return "all-controllers";
    }
}
