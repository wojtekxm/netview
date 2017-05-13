package zesp03.webapp.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.common.core.Config;
import zesp03.common.exception.ValidationException;
import zesp03.webapp.service.AdminMailService;


@Controller
public class SettingsPage {
    private static final Logger log = LoggerFactory.getLogger(SettingsPage.class);

    @Autowired
    private AdminMailService adminMailService;

    @GetMapping("/settings")
    public String getSettings(ModelMap model) {
        Config.forceReloadCustomProperties();
        model.put("examineInterval", Config.getExamineInterval());
        model.put("databaseCleaningInterval", Config.getDatabaseCleaningInterval());
        model.put("serverDelay", Config.getServerDelay());
        model.put("tokenAccessExpiration", Config.getTokenAccessExpiration());
        model.put("tokenActivateExpiraton", Config.getTokenActivateExpiraton());
        model.put("tokenPasswordExpiration", Config.getTokenPasswordExpiration());
        model.put("adminMailUsername", Config.getAdminMailUsername());
        model.put("adminMailSmtpHost", Config.getAdminMailSmtpHost());
        model.put("adminMailSmtpPort", Config.getAdminMailSmtpPort());
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
            Config.setExamineInterval(examineInterval);
            Config.setDatabaseCleaningInterval(databaseCleaningInterval);
            Config.setServerDelay(serverDelay);
            Config.setTokenAccessExpiration(tokenAccessExpiration);
            Config.setTokenActivateExpiraton(tokenActivateExpiraton);
            Config.setTokenPasswordExpiration(tokenPasswordExpiration);
            Config.setAdminMailUsername(adminMailUsername);
            Config.setAdminMailSmtpHost(adminMailSmtpHost);
            Config.setAdminMailSmtpPort(adminMailSmtpPort);
            Config.saveCustomProperties();
            Config.forceReloadCustomProperties();
            if( examineInterval == Config.getExamineInterval() &&
                    databaseCleaningInterval == Config.getDatabaseCleaningInterval() &&
                    serverDelay == Config.getServerDelay() &&
                    tokenAccessExpiration == Config.getTokenAccessExpiration() &&
                    tokenActivateExpiraton == Config.getTokenActivateExpiraton() &&
                    tokenPasswordExpiration == Config.getTokenPasswordExpiration() &&
                    Config.getAdminMailUsername().equals(adminMailUsername) &&
                    Config.getAdminMailSmtpHost().equals(adminMailSmtpHost) &&
                    adminMailSmtpPort == Config.getAdminMailSmtpPort() ) {
                success = true;
            }
        }
        catch(ValidationException exc) {
            errorField = exc.getField();
        }

        model.put("success", success);
        model.put("errorField", errorField);
        model.put("examineInterval", Config.getExamineInterval());
        model.put("databaseCleaningInterval", Config.getDatabaseCleaningInterval());
        model.put("serverDelay", Config.getServerDelay());
        model.put("tokenAccessExpiration", Config.getTokenAccessExpiration());
        model.put("tokenActivateExpiraton", Config.getTokenActivateExpiraton());
        model.put("tokenPasswordExpiration", Config.getTokenPasswordExpiration());
        model.put("adminMailUsername", Config.getAdminMailUsername());
        model.put("adminMailSmtpHost", Config.getAdminMailSmtpHost());
        model.put("adminMailSmtpPort", Config.getAdminMailSmtpPort());
        return "settings";
    }
}
