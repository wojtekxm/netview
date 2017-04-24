package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AllControllersPage {
    @GetMapping("/all-controllers")
    public String get(ModelMap model) {
        return "all-controllers";
    }
}
