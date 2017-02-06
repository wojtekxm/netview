package zesp03.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class FirstAdminCreation implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        String name = e.getServletContext().getInitParameter("first-admin-name");
        String pass = e.getServletContext().getInitParameter("first-admin-reset-password");
        if (name != null && pass != null) {
            System.out.println("first admin name = " + name);//!
            System.out.println("first admin reset password = " + pass);//!
        } else {
            System.out.println("first admin: no");//!
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
    }
}
