package zesp03.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zesp03.common.core.App;
import zesp03.common.core.Database;
import zesp03.common.entity.User;
import zesp03.common.util.Secret;
import zesp03.webapp.dto.LoginResultDto;
import zesp03.webapp.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

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
            byte[] secret = u.getSecret();
            if( u.isBlocked() ||
                    ! u.isActivated() ||
                    secret == null ||
                    ! Secret.check(secret, passToken) ) {
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

    @Override
    public UserDto authenticate(Long userId, String passToken) {
        EntityManager em = null;
        EntityTransaction tran = null;
        try {
            em = Database.createEntityManager();
            tran = em.getTransaction();
            tran.begin();

            log.info("N {}", (Object)null);//!
            User u = em.find(User.class, userId);
            if (u != null) {
                byte[] secret = u.getSecret();
                if( ! u.isBlocked() &&
                        u.isActivated() &&
                        secret != null &&
                        Secret.check(secret, passToken) ) {
                    UserDto dto = UserDto.make(u);
                    tran.commit();
                    return dto;
                }
            }

            tran.commit();
            log.info("failed authentication userId {} passToken {}", userId, passToken);
            return null;
        } catch (RuntimeException exc) {
            if (tran != null && tran.isActive()) tran.rollback();
            log.info("failed authentication userId {} passToken {}", userId, passToken);
            throw exc;
        } finally {
            if (em != null) em.close();
        }
    }
}
