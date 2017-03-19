package zesp03.webapp.service;

import org.springframework.stereotype.Service;
import zesp03.common.core.App;
import zesp03.common.core.Database;
import zesp03.common.entity.User;
import zesp03.common.util.Secret;
import zesp03.webapp.dto.LoginResultDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public LoginResultDto login(String userName, String password) {
        if(userName == null)
            throw new IllegalArgumentException("userName == null");
        if(password == null)
            throw new IllegalArgumentException("password == null");

        final String passToken = App.passwordToHash(password);
        final LoginResultDto result = new LoginResultDto(false);
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
