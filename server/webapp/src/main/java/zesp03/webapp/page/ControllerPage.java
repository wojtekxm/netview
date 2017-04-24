package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import zesp03.webapp.dto.ControllerDetailsDto;
import zesp03.webapp.service.ControllerService;

@Controller
public class ControllerPage {
    @Autowired
    private ControllerService controllerService;

    @GetMapping("/controller/{controllerId}")
    public String get(
            @PathVariable("controllerId") long controllerId,
            ModelMap model) {
        ControllerDetailsDto dto = controllerService.getDetailsOne(controllerId);
        model.put("controller", dto);
        return "controller";
    }

    @PostMapping("/controller/remove/{controllerId}")
    public String postRemove(
            @PathVariable("controllerId") long controllerId,
            ModelMap model) {
        controllerService.remove(controllerId);
        return "redirect:/all-controllers";
    }
}
