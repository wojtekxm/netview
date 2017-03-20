package zesp03.webapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import zesp03.common.core.App;
import zesp03.webapp.service.UserService;

@Component
public class SpringReadyListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger log = LoggerFactory.getLogger(SpringReadyListener.class);
    private final UserService userService;
    private boolean rootCreated;

    @Autowired
    public SpringReadyListener(UserService userService) {
        this.userService = userService;
        rootCreated = false;
    }

    @Override
    public synchronized void onApplicationEvent(ContextRefreshedEvent event) {
        if(! rootCreated) {
            String name = App.getProperty("zesp03.web.root.name");
            String password = App.getProperty("zesp03.web.root.password");
            String reset = App.getProperty("zesp03.web.root.reset");
            if( name != null &&
                    password != null &&
                    reset.equals("1") ) {
                long id = userService.makeRoot(name);
                userService.setPassword(id, password);
                rootCreated = true;
                log.info("root user with name \"{}\" has been updated", name);
            }
        }
    }
}
