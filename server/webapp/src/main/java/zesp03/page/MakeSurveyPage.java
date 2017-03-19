package zesp03.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MakeSurveyPage {
    @GetMapping("/make-survey")
    public String get() {
        return "make-survey";
    }
}
