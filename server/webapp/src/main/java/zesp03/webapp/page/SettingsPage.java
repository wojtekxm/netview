package zesp03.webapp.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.core.App;

@Controller
public class SettingsPage {
    @GetMapping("/settings")
    public String getSettings(ModelMap model) {
        App.reloadCustomProperties();
        model.put("examineInterval", App.getExamineInterval());
        return "settings";
    }

    @PostMapping(value = "/settings", consumes = "application/x-www-form-urlencoded")
    public String postSettings(
            @RequestParam("examineInterval") int examineInterval,
            ModelMap model) {
        App.setExamineInterval(examineInterval);
        App.saveCustomProperties();
        App.reloadCustomProperties();
        if(examineInterval == App.getExamineInterval()) {
            model.put("success", true);
        }
        else {
            model.put("success", false);
        }
        model.put("examineInterval", App.getExamineInterval());
        return "settings";
    }
}
