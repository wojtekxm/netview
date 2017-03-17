package zesp03.config;

import zesp03.common.App;
import zesp03.exception.BaseException;
import zesp03.service.UserService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class RootCreation implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        String name = App.getProperty("zesp03.web.root.name");
        String password = App.getProperty("zesp03.web.root.password");
        if(name != null && password != null) {
            UserService ser = new UserService();
            try {
                long id = ser.makeRoot(name);
                ser.setPassword(id, password);
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
