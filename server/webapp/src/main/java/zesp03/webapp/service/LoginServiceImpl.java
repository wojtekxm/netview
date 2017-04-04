package zesp03.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.core.App;
import zesp03.common.entity.User;
import zesp03.common.repository.UserRepository;
import zesp03.common.util.Secret;
import zesp03.webapp.dto.LoginResultDto;
import zesp03.webapp.dto.UserDto;

import java.util.List;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public LoginResultDto login(String userName, String password) {
        final String passToken = App.passwordToHash(password);
        List<User> list = userRepository.findByName(userName);
        if(list.isEmpty()) {
            return null;
        }
        User u = list.get(0);
        byte[] secret = u.getSecret();
        if( u.isBlocked() ||
                ! u.isActivated() ||
                secret == null ||
                ! Secret.check(secret, passToken) ) {
            return null;
        }
        LoginResultDto r = new LoginResultDto();
        r.setUserId(u.getId());
        r.setPassToken(passToken);
        return r;
    }

    @Override
    public UserDto authenticate(Long userId, String passToken) {
        User u = userRepository.findOne(userId);
        if (u != null) {
            byte[] secret = u.getSecret();
            if (!u.isBlocked() &&
                    u.isActivated() &&
                    secret != null &&
                    Secret.check(secret, passToken)) {
                return UserDto.make(u);
            }
        }
        log.info("failed authentication userId {} passToken {}", userId, passToken);
        return null;
    }
}
