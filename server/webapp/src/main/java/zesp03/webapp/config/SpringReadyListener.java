/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import zesp03.common.core.Config;
import zesp03.webapp.service.LoginService;
import zesp03.webapp.service.UserService;

@Component
public class SpringReadyListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger log = LoggerFactory.getLogger(SpringReadyListener.class);
    private final UserService userService;
    private boolean rootCreated;

    @Autowired
    public SpringReadyListener(UserService userService, LoginService loginService) {
        this.userService = userService;
        UglyUserServiceHolder.setLoginService(loginService);
        rootCreated = false;
    }

    @Override
    public synchronized void onApplicationEvent(ContextRefreshedEvent event) {
        if(! rootCreated) {
            String name = Config.getRootResetName();
            String password = Config.getRootResetPassword();
            boolean reset = Config.isRootResetEnabled();
            if( name != null &&
                    password != null &&
                    reset ) {
                userService.makeRoot(name, password);
                rootCreated = true;
                log.info("root user with name \"{}\" has been updated", name);
            }
        }
    }
}
