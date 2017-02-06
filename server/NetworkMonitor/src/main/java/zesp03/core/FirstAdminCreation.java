package zesp03.core;

import zesp03.entity.User;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

public class FirstAdminCreation implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        String name = e.getServletContext().getInitParameter("first-admin-name");
        String password = e.getServletContext().getInitParameter("first-admin-reset-password");
        if (name != null && password != null) {
            String hash = App.passwordToHash(password);
            Secret s = Secret.create(hash.toCharArray(), 1);
            System.out.println("first admin name = " + name);//!
            System.out.println("first admin reset password = " + password);//!
            System.out.println("hash = " + hash);//!
            Database.run(em -> {
                List<User> list = em.createQuery("SELECT u FROM User u WHERE u.name = :n", User.class)
                        .setParameter("n", name)
                        .getResultList();
                if (list.isEmpty()) {
                    User u = new User();
                    u.setName(name);
                    u.setSecret(s.getData());
                    u.setIsAdmin(true);
                    em.persist(u);
                } else {
                    User u = list.get(0);
                    u.setName(name);
                    u.setSecret(s.getData());
                    u.setIsAdmin(true);
                    em.merge(u);
                }
            });
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
    }
}
