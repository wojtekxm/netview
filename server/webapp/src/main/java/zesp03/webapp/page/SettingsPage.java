package zesp03.webapp.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.core.App;
import zesp03.common.exception.ValidationException;
import zesp03.webapp.service.AdminMailService;


@Controller
public class SettingsPage {
    private static final Logger log = LoggerFactory.getLogger(SettingsPage.class);

    @Autowired
    private AdminMailService adminMailService;

    @GetMapping("/settings")
    public String getSettings(ModelMap model) {
        App.forceReloadCustomProperties();
        model.put("examineInterval", App.getExamineInterval());
        model.put("databaseCleaningInterval", App.getDatabaseCleaningInterval());
        model.put("serverDelay", App.getServerDelay());
        model.put("tokenAccessExpiration", App.getTokenAccessExpiration());
        model.put("tokenActivateExpiraton", App.getTokenActivateExpiraton());
        model.put("tokenPasswordExpiration", App.getTokenPasswordExpiration());
        model.put("adminMailUsername", App.getAdminMailUsername());
        model.put("adminMailSmtpHost", App.getAdminMailSmtpHost());
        model.put("adminMailSmtpPort", App.getAdminMailSmtpPort());
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
            @RequestParam("adminMailUsername") String adminMailUsername,
            @RequestParam("adminMailSmtpHost") String adminMailSmtpHost,
            @RequestParam("adminMailSmtpPort") int adminMailSmtpPort,
            ModelMap model) {
        boolean success = false;
        String errorField = null;
        try {
            App.setExamineInterval(examineInterval);
            App.setDatabaseCleaningInterval(databaseCleaningInterval);
            App.setServerDelay(serverDelay);
            App.setTokenAccessExpiration(tokenAccessExpiration);
            App.setTokenActivateExpiraton(tokenActivateExpiraton);
            App.setTokenPasswordExpiration(tokenPasswordExpiration);
            App.setAdminMailUsername(adminMailUsername);
            App.setAdminMailSmtpHost(adminMailSmtpHost);
            App.setAdminMailSmtpPort(adminMailSmtpPort);
            App.saveCustomProperties();
            App.forceReloadCustomProperties();
            if( examineInterval == App.getExamineInterval() &&
                    databaseCleaningInterval == App.getDatabaseCleaningInterval() &&
                    serverDelay == App.getServerDelay() &&
                    tokenAccessExpiration == App.getTokenAccessExpiration() &&
                    tokenActivateExpiraton == App.getTokenActivateExpiraton() &&
                    tokenPasswordExpiration == App.getTokenPasswordExpiration() &&
                    App.getAdminMailUsername().equals(adminMailUsername) &&
                    App.getAdminMailSmtpHost().equals(adminMailSmtpHost) &&
                    adminMailSmtpPort == App.getAdminMailSmtpPort() ) {
                success = true;
            }
        }
        catch(ValidationException exc) {
            errorField = exc.getField();
        }

        model.put("success", success);
        model.put("errorField", errorField);
        model.put("examineInterval", App.getExamineInterval());
        model.put("databaseCleaningInterval", App.getDatabaseCleaningInterval());
        model.put("serverDelay", App.getServerDelay());
        model.put("tokenAccessExpiration", App.getTokenAccessExpiration());
        model.put("tokenActivateExpiraton", App.getTokenActivateExpiraton());
        model.put("tokenPasswordExpiration", App.getTokenPasswordExpiration());
        model.put("adminMailUsername", App.getAdminMailUsername());
        model.put("adminMailSmtpHost", App.getAdminMailSmtpHost());
        model.put("adminMailSmtpPort", App.getAdminMailSmtpPort());
        return "settings";
    }
}
