package zesp03.core;

import zesp03.entity.User;
import zesp03.entity.UserRole;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

public class RootCreation implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        String name = e.getServletContext().getInitParameter("root-name");
        String password = e.getServletContext().getInitParameter("root-password");
        if (name != null && password != null) {
            if (!App.isValidUserName(name))
                throw new IllegalStateException("invalid root-name");
            String hash = App.passwordToHash(password);
            Secret s = Secret.create(hash.toCharArray(), 1);
            Database.run(em -> {
                List<User> list = em.createQuery("SELECT u FROM User u WHERE u.role = 'ROOT'", User.class).getResultList();
                for (User u : list) {
                    u.setRole(UserRole.ADMIN);
                    em.merge(u);
                }
                list = em.createQuery("SELECT u FROM User u WHERE u.name = :n", User.class)
                        .setParameter("n", name)
                        .getResultList();
                if (list.isEmpty()) {
                    User u = new User();
                    u.setName(name);
                    u.setSecret(s.getData());
                    u.setActivated(true);
                    u.setBlocked(false);
                    u.setRole(UserRole.ROOT);
                    em.persist(u);
                } else {
                    User u = list.get(0);
                    u.setName(name);
                    u.setSecret(s.getData());
                    u.setActivated(true);
                    u.setBlocked(false);
                    u.setRole(UserRole.ROOT);
                    em.merge(u);
                }
            });
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
    }
}
