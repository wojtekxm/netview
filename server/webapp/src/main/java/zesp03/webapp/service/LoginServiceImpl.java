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
import zesp03.common.exception.AccessException;
import zesp03.common.exception.ValidationException;
import zesp03.common.repository.TokenRepository;
import zesp03.common.repository.UserRepository;
import zesp03.common.util.Secret;
import zesp03.webapp.dto.AccessDto;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.dto.input.ChangePasswordDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public AccessDto grantAccess(User user) {
        final String key = generateToken(32);
        final Secret accessSecret = Secret.create(key.toCharArray(), 1);
        final Instant now = Instant.now();
        final Instant expires = now.plusSeconds( Config.getTokenAccessExpiration() * 60 );

        final Token token = new Token();
        token.setAction(TokenAction.ACCESS);
        token.setUser(user);
        token.setExpires(Date.from(expires));
        token.setSecret(accessSecret.getData());
        tokenRepository.save(token);
        user.setLastAccess(Date.from(now));

        final AccessDto dto = new AccessDto();
        dto.setUserId(token.getId());//tak!
        dto.setPassToken(key);//tak!
        return dto;
    }

    @Override
    public boolean checkLogin(User user, String password) {
        if(user == null) {
            return false;
        }
        if(user.isBlocked()) {
            return false;
        }
        if(! user.isActivated()) {
            return false;
        }
        if(user.getSecret() == null) {
            return false;
        }
        if(password == null || password.length() < 1) {
            return false;
        }
        if(! Secret.check(user.getSecret(), password)) {
            return false;
        }
        return true;
    }

    @Override
    public AccessDto login(String userName, String password) {
        final List<User> list = userRepository.findByName(userName);
        if(list.isEmpty()) {
            log.info("failed login (user not found), userName={}", userName);
            return null;
        }
        final User user = list.get(0);
        if(! checkLogin(user, password)) {
            log.info("failed login, userName={}", userName);
            return null;
        }
        log.info("successful login, userName={}", userName);
        return grantAccess(user);
    }

    @Override
    public void logout(Long tokenId) {
        final Token token = tokenRepository.findOne(tokenId);
        if(token == null) {
            log.info("failed logout (token not found), tokenId={}", tokenId);
            return;
        }
        if(token.getAction() != TokenAction.ACCESS) {
            log.info("failed logout (wrong token action), tokenId={}", tokenId);
        }
        tokenRepository.delete(token);
    }

    @Override
    public UserDto authenticate(Long tokenId, String tokenValue) {
        final Instant now = Instant.now();
        final Date date = Date.from(now);
        final Token token = tokenRepository.findOne(tokenId);
        if(token == null) {
            log.info("failed authentication: token not found, tokenId={} tokenValue={}", tokenId, tokenValue);
            return null;
        }
        if(! token.checkValid()) {
            log.info("failed authentication: token expired, tokenId={} tokenValue={}", tokenId, tokenValue);
            return null;
        }
        if(token.getAction() != TokenAction.ACCESS) {
            log.info("failed authentication: wrong token action, tokenId={} tokenValue={}", tokenId, tokenValue);
            return null;
        }
        if(token.getSecret() == null) {
            log.info("failed authentication: token has no secret, tokenId={} tokenValue={}", tokenId, tokenValue);
            return null;
        }

        final User user = token.getUser();
        if(user == null) {
            log.info("failed authentication: user not found, tokenId={} tokenValue={}", tokenId, tokenValue);
            return null;
        }
        if(user.isBlocked()) {
            log.info("failed authentication: user is blocked, tokenId={} tokenValue={}", tokenId, tokenValue);
            return null;
        }
        if(! user.isActivated()) {
            log.info("failed authentication: user is not activated, tokenId={} tokenValue={}", tokenId, tokenValue);
            return null;
        }
        if(user.getSecret() == null) {
            log.info("failed authentication: user has no secret, tokenId={} tokenValue={}", tokenId, tokenValue);
            return null;
        }
        if(! Secret.check(token.getSecret(), tokenValue)) {
            log.info("failed authentication: token - secret mismatch, tokenId={} tokenValue={}", tokenId, tokenValue);
            return null;
        }

        user.setLastAccess(date);
        token.setExpires(
                Date.from(
                        now.plusSeconds(
                                Config.getTokenAccessExpiration() * 60
                        )
                )
        );
        return UserDto.make(user);
    }

    @Override
    public void setPassword(User user, String password) {
        if(password == null || password.isEmpty()) {
            throw new ValidationException("password", "empty password");
        }
        Secret s = Secret.create(password.toCharArray(), 1);
        user.setSecret(s.getData());
    }

    @Override
    public void changePassword(Long userId, ChangePasswordDto dto) {
        final String old = dto.getOld();
        final String desired = dto.getDesired();
        final String repeat = dto.getRepeat();
        if(old == null) {
            throw new ValidationException("old", "empty");
        }
        if(desired == null) {
            throw new ValidationException("desired", "empty");
        }
        if(repeat == null) {
            throw new ValidationException("repeat", "empty");
        }
        if( ! desired.equals(repeat) ) {
            throw new ValidationException("repeat", "passwords do not match");
        }

        final User user = userRepository.findOne(userId);
        if(! checkLogin(user, old)) {
            throw new AccessException("invalid login data");
        }
        setPassword(user, desired);
    }

    @Deprecated
    public void removeExpiredTokens() {
        em.createQuery("DELETE FROM Token t WHERE t.expires <= :now")
                .setParameter("now", new Date())
                .executeUpdate();
    }

    @Override
    public String passwordToHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] binaryPassword = password.getBytes("UTF-8");
            byte[] binaryHash = md.digest(binaryPassword);
            return Base64.getUrlEncoder().encodeToString(binaryHash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exc) {
            throw new IllegalStateException(exc);
        }
    }

    @Override
    public String generateToken(int randomBytes) {
        byte[] bin = new byte[randomBytes];
        new SecureRandom().nextBytes(bin);
        return Base64.getUrlEncoder().encodeToString(bin);
    }
}
