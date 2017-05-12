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
        App.forceReloadCustomProperties();
        model.put("examineInterval", App.getExamineInterval());
        model.put("databaseCleaningInterval", App.getDatabaseCleaningInterval());
        model.put("serverDelay", App.getServerDelay());
        model.put("tokenAccessExpiration", App.getTokenAccessExpiration());
        model.put("tokenActivateExpiraton", App.getTokenActivateExpiraton());
        model.put("tokenPasswordExpiration", App.getTokenPasswordExpiration());
        return "settings";
    }

    @PostMapping(value = "/settings", consumes = "application/x-www-form-urlencoded")
    public String postSettings(
            @RequestParam("examineInterval") int examineInterval,
            @RequestParam("databaseCleaningInterval") int databaseCleaningInterval,
            @RequestParam("serverDelay") int serverDelay,
            @RequestParam("tokenAccessExpiration") int tokenAccessExpiration,
            @RequestParam("tokenActivateExpiraton") int tokenActivateExpiraton,
            @RequestParam("tokenPasswordExpiration") int tokenPasswordExpiration,
            ModelMap model) {
        App.setExamineInterval(examineInterval);
        App.setDatabaseCleaningInterval(databaseCleaningInterval);
        App.setServerDelay(serverDelay);
        App.setTokenAccessExpiration(tokenAccessExpiration);
        App.setTokenActivateExpiraton(tokenActivateExpiraton);
        App.setTokenPasswordExpiration(tokenPasswordExpiration);
        App.saveCustomProperties();
        App.forceReloadCustomProperties();
        if( examineInterval == App.getExamineInterval() &&
                databaseCleaningInterval == App.getDatabaseCleaningInterval() &&
                serverDelay == App.getServerDelay() &&
                tokenAccessExpiration == App.getTokenAccessExpiration() &&
                tokenActivateExpiraton == App.getTokenActivateExpiraton() &&
                tokenPasswordExpiration == App.getTokenPasswordExpiration() ) {
            model.put("success", true);
        }
        else {
            model.put("success", false);
        }
        model.put("examineInterval", App.getExamineInterval());
        model.put("databaseCleaningInterval", App.getDatabaseCleaningInterval());
        model.put("serverDelay", App.getServerDelay());
        model.put("tokenAccessExpiration", App.getTokenAccessExpiration());
        model.put("tokenActivateExpiraton", App.getTokenActivateExpiraton());
        model.put("tokenPasswordExpiration", App.getTokenPasswordExpiration());
        return "settings";
    }
}
