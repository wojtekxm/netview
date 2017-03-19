package zesp03.webapp.service;

import zesp03.common.core.App;
import zesp03.common.core.Database;
import zesp03.common.entity.Token;
import zesp03.common.entity.TokenAction;
import zesp03.common.entity.User;
import zesp03.common.entity.UserRole;
import zesp03.common.exception.AccessException;
import zesp03.common.exception.NotFoundException;
import zesp03.common.exception.ValidationException;
import zesp03.common.util.Secret;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class UserService {
    // zwraca id roota
    public long makeRoot(String userName) {
        if(userName == null || userName.isEmpty())
            throw new ValidationException("username", "invalid root username");
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
                u.setActivated(true);
                u.setBlocked(false);
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
        if( desired == null || desired.isEmpty() )
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

    public boolean checkActivation(Long tokenId, String tokenValue) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Token token = em.find(Token.class, tokenId);
            boolean result = token != null &&
                    token.getAction() == TokenAction.ACTIVATE_ACCOUNT &&
                    token.getUser() != null &&
                    !token.getUser().isActivated() &&
                    Secret.check(token.getSecret(), tokenValue);
            tran.commit();
            return result;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }

    public void activateAccount(Long tokenId, String tokenValue,
            String userName, String password, String repeatPassword) {
        if(! password.equals(repeatPassword))
            throw new ValidationException("repeatPassword", "does not match");
        String passwordHash = App.passwordToHash(password);
        byte[] userSecret = Secret.create(passwordHash.toCharArray(), 1).getData();

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            Token token = em.find(Token.class, tokenId);
            if(token == null)
                throw new NotFoundException("token");
            User user = token.getUser();
            if(user == null)
                throw new NotFoundException("user");
            if(token.getAction() != TokenAction.ACTIVATE_ACCOUNT)
                throw new ValidationException("token", "invalid token type");
            if(token.getUser().isActivated())
                throw new ValidationException("user", "already activated");
            if(!Secret.check(token.getSecret(), tokenValue))
                throw new ValidationException("token", "invalid value");
            user.setName(userName);
            user.setSecret(userSecret);
            user.setActivated(true);
            user.setBlocked(false);
            em.merge(user);
            em.remove(token);
            tran.commit();
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
}
