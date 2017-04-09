package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FakeSurveysPage {
    @GetMapping("/fake-surveys")
    public String get() {
        return "fake-surveys";
    }
}
