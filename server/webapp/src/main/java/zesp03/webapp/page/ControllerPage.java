package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.webapp.dto.ControllerDetailsDto;
import zesp03.webapp.service.ControllerService;

@Controller
public class ControllerPage {
    @Autowired
    private ControllerService controllerService;

    @GetMapping("/controller")
    public String get(
            @RequestParam("id") long id,
            ModelMap model) {
        ControllerDetailsDto dto = controllerService.getDetailsOne(id);
        model.put("controller", dto);
        return "controller";
    }
}
