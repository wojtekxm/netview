package zesp03.service;

import zesp03.common.App;
import zesp03.common.Database;
import zesp03.dto.LoginResultDto;
import zesp03.entity.User;
import zesp03.util.Secret;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class LoginService {
    public LoginResultDto login(String userName, String password) {
        if(userName == null)
            throw new IllegalArgumentException("userName == null");
        if(password == null)
            throw new IllegalArgumentException("password == null");

        final String passToken = App.passwordToHash(password);
        final LoginResultDto result = new LoginResultDto();
        result.setSuccess(false);
        result.setUserId(-1);
        result.setPassToken(null);

        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            List<User> list = em.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
                    .setParameter("name", userName)
                    .getResultList();
            if(list.isEmpty()) {
                tran.rollback();
                return result;
            }
            User u = list.get(0);
            if( u.isBlocked() ||
                    ! u.isActivated() ||
                    u.getSecret() == null ||
                    ! Secret.check(u.getSecret(), passToken) ) {
                tran.rollback();
                return result;
            }
            result.setSuccess(true);
            result.setUserId(u.getId());
            result.setPassToken(passToken);

            tran.commit();
            return result;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
}
