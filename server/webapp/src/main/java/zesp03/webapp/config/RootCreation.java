package zesp03.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zesp03.common.core.App;
import zesp03.common.exception.BaseException;
import zesp03.webapp.service.UserService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class RootCreation implements ServletContextListener {
    @Autowired
    private UserService userService;

    @Override
    public void contextInitialized(ServletContextEvent e) {
        String name = App.getProperty("zesp03.web.root.name");
        String password = App.getProperty("zesp03.web.root.password");
        if(name != null && password != null) {
            try {
                long id = userService.makeRoot(name);
                userService.setPassword(id, password);
            }
            catch(BaseException exc) {
                throw new IllegalStateException(exc);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
    }
}
