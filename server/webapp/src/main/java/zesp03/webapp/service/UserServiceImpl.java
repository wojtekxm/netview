package zesp03.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.core.Config;
import zesp03.common.entity.Token;
import zesp03.common.entity.TokenAction;
import zesp03.common.entity.User;
import zesp03.common.entity.UserRole;
import zesp03.common.exception.AccessException;
import zesp03.common.exception.NotFoundException;
import zesp03.common.exception.ValidationException;
import zesp03.common.repository.TokenRepository;
import zesp03.common.repository.UserRepository;
import zesp03.common.util.Secret;
import zesp03.common.util.Validator;
import zesp03.webapp.dto.UserCreatedDto;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.dto.input.ActivateUserDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private LoginService loginService;

    @Autowired
    private AdminMailService adminMailService;

    @Autowired
    private Validator validator;

    @Override
    public UserDto getOne(Long userId) {
        User u = userRepository.findOne(userId);
        if(u == null) {
            throw new NotFoundException("user");
        }
        return UserDto.make(u);
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> list = new ArrayList<>();
        for(User u : userRepository.findAll()) {
            list.add( UserDto.make(u) );
        }
        return list;
    }

    @Override
    public void block(Long userId, Long callerId) {
        if( userId.equals(callerId) ) {
            throw new AccessException("you can not block yourself");
        }
        User u = userRepository.findOne(userId);
        if (u == null) {
            throw new NotFoundException("user");
        }
        u.setBlocked(true);
    }

    @Override
    public void unlock(Long userId, Long callerId) {
        if( userId.equals(callerId) ) {
            throw new AccessException("you can not unlock yourself");
        }
        User u = userRepository.findOne(userId);
        if (u == null) {
            throw new NotFoundException("user");
        }
        u.setBlocked(false);
    }

    @Override
    public void remove(Long userId, Long callerId) {
        if( userId.equals(callerId) ) {
            throw new AccessException("you can not delete yourself");
        }
        User u = userRepository.findOne(userId);
        if (u == null) {
            throw new NotFoundException("user");
        }
        userRepository.delete(u);
    }

    @Override
    public void advance(Long userId, Long callerId) {
        if( userId.equals(callerId) ) {
            throw new AccessException("you can not advance yourself");
        }
        User u = userRepository.findOne(userId);
        if (u == null) {
            throw new NotFoundException("user");
        }
        u.setRole(UserRole.ROOT);
    }

    @Override
    public void degrade(Long userId, Long callerId) {
        if( userId.equals(callerId) ) {
            throw new AccessException("you can not degrade yourself");
        }
        User u = userRepository.findOne(userId);
        if (u == null) {
            throw new NotFoundException("user");
        }
        u.setRole(UserRole.NORMAL);
    }

    @Override
    public UserCreatedDto create(String sendEmail, String serverName, int serverPort) {
        final String tokenValue = loginService.generateToken(32);
        final Secret secret = Secret.create(tokenValue.toCharArray(), 1);
        final Instant now = Instant.now();
        final Instant expires = now.plusSeconds(Config.getTokenActivateExpiraton() * 60);

        User u = new User();
        u.setName(null);
        u.setSecret(null);
        u.setActivated(false);
        u.setBlocked(true);
        u.setRole(UserRole.NORMAL);
        u.setLastAccess(Date.from(now));
        userRepository.save(u);

        Token t = new Token();
        t.setSecret(secret.getData());
        t.setAction(TokenAction.ACTIVATE_ACCOUNT);
        t.setUser(u);
        t.setExpires(Date.from(expires));
        tokenRepository.save(t);

        UserCreatedDto r = new UserCreatedDto();
        r.setTokenId(t.getId());
        r.setTokenValue(tokenValue);
        r.setUserId(u.getId());
        try {
            String url = new StringBuilder()
                    .append("http://")
                    .append(serverName)
                    .append(serverPort != 80 ? ":" + serverPort : "")
                    .append("/activate-account?tid=")
                    .append(r.getTokenId())
                    .append("&tv=")
                    .append(URLEncoder.encode(r.getTokenValue(), "utf-8"))
                    .toString();
            r.setActivationURL(url);
        }
        catch(UnsupportedEncodingException exc) {
            throw new IllegalStateException(exc);
        }
        if(sendEmail != null) {
            if(validator.checkEmail(sendEmail)) {
                String subject = "Aktywacja konta w NetView";
                String html = "By aktywować konto kliknij w poniższy link\n" + r.getActivationURL();
                if (!adminMailService.send(sendEmail, subject, html)) {
                    log.warn("failed to send activation e-mail to {}", sendEmail);
                }
            }
            else {
                log.debug("invalid e-mail {}", sendEmail);
            }
        }
        return r;
    }

    @Override
    public void makeRoot(String userName, String password) {
        if(userName == null || userName.isEmpty()) {
            throw new ValidationException("username", "empty root username");
        }
        em.createQuery("UPDATE User u SET u.role = 'NORMAL' WHERE u.role = 'ROOT'")
                .executeUpdate();
        List<User> list = userRepository.findByName(userName);
        User user;
        if(list.isEmpty()) {
            user = new User();
            user.setSecret(null);
        }
        else {
            user = list.get(0);
        }
        user.setLastAccess(new Date());
        user.setName(userName);
        user.setActivated(true);
        user.setBlocked(false);
        user.setRole(UserRole.ROOT);
        userRepository.save(user);
        loginService.setPassword(user, password);
    }

    @Override
    public boolean checkActivation(Long tokenId, String tokenValue) {
        Token token = tokenRepository.findOne(tokenId);
        return token != null &&
                token.getAction() == TokenAction.ACTIVATE_ACCOUNT &&
                token.checkValid() &&
                token.getUser() != null &&
                !token.getUser().isActivated() &&
                Secret.check(token.getSecret(), tokenValue);
    }

    @Override
    public void activate(ActivateUserDto dto) {
        final long tokenId = dto.getTokenId();
        final String tokenValue = dto.getTokenValue();
        final String username = dto.getUsername();
        final String password = dto.getPassword();
        final String repeatPassword = dto.getRepeatPassword();

        if(tokenValue == null) {
            throw new ValidationException("token value", "null");
        }
        if(username == null || username.length() < 1) {
            throw new ValidationException("username", "empty name");
        }
        if(password == null) {
            throw new ValidationException("password", "null");
        }
        if(repeatPassword == null) {
            throw new ValidationException("repeatPassword", "null");
        }
        if( ! repeatPassword.equals( password ) ) {
            throw new ValidationException("repeatPassword", "does not match");
        }

        //TODO co jak nazwa użytkownika zajęta
        Token token = tokenRepository.findOne(tokenId);
        if(token == null) {
            throw new NotFoundException("token");
        }
        if(! token.checkValid()) {
            throw new AccessException("token expired");
        }
        if(token.getAction() != TokenAction.ACTIVATE_ACCOUNT) {
            throw new ValidationException("token", "invalid token type");
        }
        final User user = token.getUser();
        if(user == null) {
            throw new NotFoundException("user");
        }
        if(user.isActivated()) {
            throw new ValidationException("user", "already activated");
        }
        if(!Secret.check(token.getSecret(), tokenValue)) {
            throw new ValidationException("token", "invalid value");
        }
        user.setName(username);
        user.setActivated(true);
        user.setBlocked(false);
        user.setLastAccess(new Date());
        loginService.setPassword(user, password);
        token.setExpires(new Date(0L));
    }
}
