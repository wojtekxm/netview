package zesp03.service;

import zesp03.common.App;
import zesp03.common.Database;
import zesp03.entity.User;
import zesp03.entity.UserRole;
import zesp03.exception.AccessException;
import zesp03.exception.NotFoundException;
import zesp03.exception.ValidationException;
import zesp03.util.Secret;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class UserService {
    public long makeRoot(String userName) {
        if( ! App.isValidUserName(userName) ) {
            throw new ValidationException("username", "invalid root username");
        }
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            em.createQuery("UPDATE User u SET u.role = 'ADMIN' WHERE u.role = 'ROOT'").executeUpdate();
            List<User> list = em.createQuery("SELECT u FROM User u WHERE u.name = :n", User.class)
                    .setParameter("n", userName)
                    .getResultList();
            User u;
            if (list.isEmpty()) {
                u = new User();
                u.setName(userName);
                u.setSecret(null);
                u.setActivated(true);
                u.setBlocked(false);
                u.setRole(UserRole.ROOT);
                em.persist(u);
            } else {
                u = list.get(0);
                u.setName(userName);
                u.setRole(UserRole.ROOT);
                em.merge(u);
            }

            tran.commit();
            return u.getId();

        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    public void setPassword(long userId, String password) {
        if(password == null || password.isEmpty())
            throw new ValidationException("password", "blank password");
        String hash = App.passwordToHash(password);
        Secret s = Secret.create(hash.toCharArray(), 1);

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = em.find(User.class, userId);
            if(u == null)
                throw new NotFoundException("no such user");
            u.setSecret(s.getData());
            em.merge(u);

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * @param userId id użytkownika
     * @param old stare hasło
     * @param desired nowe hasło
     * @param repeat powtórzenie nowego hasła
     * @return Passtoken dla nowego hasła jeśli uda się zmienić hasło (jak się nie uda to wyrzuca wyjątek).
     */
    public String changePassword(
            long userId, String old, String desired, String repeat) {
        if( ! App.isValidPassword(desired) )
            throw new ValidationException("desired", "blank password");
        if( ! desired.equals(repeat) )
            throw new ValidationException("repeat", "passwords do not match");

        String oldHash = App.passwordToHash(old);
        String desiredHash = App.passwordToHash(desired);
        Secret desiredSecret = Secret.create(desiredHash.toCharArray(), 1);

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            User u = em.find(User.class, userId);
            if(u == null)
                throw new NotFoundException("no such user");
            if( u.getSecret() == null )
                throw new AccessException("failed to confirm password, because user don't have one");
            if( ! Secret.check( u.getSecret(), oldHash )  )
                throw new AccessException("invalid old password");
            u.setSecret( desiredSecret.getData() );
            em.merge( u );

            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
        return desiredHash;
    }
}
