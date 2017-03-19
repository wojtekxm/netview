package zesp03.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusPage {
    @GetMapping("/status")
    public String get(ModelMap model) {
        return "status";
    }
}
