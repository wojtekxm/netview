package zesp03.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import zesp03.webapp.dto.AccessDto;
import zesp03.webapp.dto.UserCreatedDto;
import zesp03.webapp.dto.UserDto;
import zesp03.webapp.dto.input.ActivateUserDto;
import zesp03.webapp.dto.input.ChangePasswordDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
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
    public void block(Long userId) {
        User u = userRepository.findOne(userId);
        if (u == null) {
            throw new NotFoundException("user");
        }
        u.setBlocked(true);
        userRepository.save(u);
    }

    @Override
    public void remove(Long userId) {
        User u = userRepository.findOne(userId);
        if (u == null) {
            throw new NotFoundException("user");
        }
        userRepository.delete(u);
    }

    @Override
    public UserCreatedDto create(String serverName, int serverPort) {
        String tokenValue = generateToken(32);
        Secret secret = Secret.create(tokenValue.toCharArray(), 1);

        User u = new User();
        u.setName(null);
        u.setSecret(null);
        u.setActivated(false);
        u.setBlocked(true);
        u.setRole(UserRole.NORMAL);
        userRepository.save(u);

        Token t = new Token();
        t.setSecret(secret.getData());
        t.setAction(TokenAction.ACTIVATE_ACCOUNT);
        t.setUser(u);
        tokenRepository.save(t);

        UserCreatedDto r = new UserCreatedDto();
        r.setTokenId(t.getId());
        r.setTokenValue(tokenValue);
        r.setUserId(u.getId());
        r.setActivationURL(
                new StringBuilder()
                        .append("http://")
                        .append(serverName)
                        .append(serverPort != 80 ? ":" + serverPort : "")
                        .append("/activate-account?tid=")
                        .append(r.getTokenId())
                        .append("&tv=")
                        .append(r.getTokenValue())
                        .toString()
        );
        return r;
    }

    @Override
    public long makeRoot(String userName) {
        if(userName == null || userName.isEmpty()) {
            throw new ValidationException("username", "empty root username");
        }
        em.createQuery("UPDATE User u SET u.role = 'NORMAL' WHERE u.role = 'ROOT'")
                .executeUpdate();
        List<User> list = userRepository.findByName(userName);
        User u;
        if(list.isEmpty()) {
            u = new User();
            u.setName(userName);
            u.setSecret(null);
            u.setActivated(true);
            u.setBlocked(false);
            u.setRole(UserRole.ROOT);
            userRepository.save(u);
        }
        else {
            u = list.get(0);
            u.setName(userName);
            u.setActivated(true);
            u.setBlocked(false);
            u.setRole(UserRole.ROOT);
            userRepository.save(u);
        }
        return u.getId();
    }

    @Override
    public void setPassword(Long userId, String password) {
        if(password == null || password.isEmpty()) {
            throw new ValidationException("password", "empty password");
        }

        String hash = passwordToHash(password);
        Secret s = Secret.create(hash.toCharArray(), 1);
        User u = userRepository.findOne(userId);
        if(u == null) {
            throw new NotFoundException("user");
        }
        u.setSecret(s.getData());
        userRepository.save(u);
    }

    @Override
    public AccessDto changePassword(Long userId, ChangePasswordDto dto) {
        final String old = dto.getOld();
        final String desired = dto.getDesired();
        final String repeat = dto.getRepeat();

        if( desired == null || desired.isEmpty() ) {
            throw new ValidationException("desired", "empty password");
        }
        if( ! desired.equals(repeat) ) {
            throw new ValidationException("repeat", "passwords do not match");
        }

        String oldHash = passwordToHash(old);
        String desiredHash = passwordToHash(desired);
        Secret desiredSecret = Secret.create(desiredHash.toCharArray(), 1);
        User u = userRepository.findOne(userId);
        if(u == null) {
            throw new NotFoundException("user");
        }
        if( u.getSecret() == null ) {
            throw new AccessException("failed to confirm password, because user don't have one");
        }
        if( ! Secret.check( u.getSecret(), oldHash )  ) {
            throw new AccessException("invalid old password");
        }
        u.setSecret( desiredSecret.getData() );
        userRepository.save(u);
        AccessDto result = new AccessDto();
        result.setUserId(u.getId());
        result.setPassToken(desiredHash);
        return result;
    }

    @Override
    public boolean checkActivation(Long tokenId, String tokenValue) {
        Token token = tokenRepository.findOne(tokenId);
        return token != null &&
                token.getAction() == TokenAction.ACTIVATE_ACCOUNT &&
                token.getUser() != null &&
                !token.getUser().isActivated() &&
                Secret.check(token.getSecret(), tokenValue);
    }

    @Override
    public void activate(ActivateUserDto dto) {
        if(dto.getTokenValue() == null) {
            throw new ValidationException("token value", "null");
        }
        if(dto.getUsername() == null || dto.getUsername().length() < 1) {
            throw new ValidationException("username", "empty name");
        }
        if(dto.getPassword() == null || dto.getPassword().length() < 4) {
            throw new ValidationException("password", "too short");
        }
        if( ! dto.getRepeatPassword().equals( dto.getPassword() ) ) {
            throw new ValidationException("repeatPassword", "does not match");
        }
        String passwordHash = passwordToHash(dto.getPassword());
        byte[] userSecret = Secret.create(passwordHash.toCharArray(), 1).getData();

        //TODO co jak nazwa użytkownika zajęta
        Token token = tokenRepository.findOne(dto.getTokenId());
        if(token == null) {
            throw new NotFoundException("token");
        }
        User user = token.getUser();
        if(user == null) {
            throw new NotFoundException("user");
        }
        if(token.getAction() != TokenAction.ACTIVATE_ACCOUNT) {
            throw new ValidationException("token", "invalid token type");
        }
        if(token.getUser().isActivated()) {
            throw new ValidationException("user", "already activated");
        }
        if(!Secret.check(token.getSecret(), dto.getTokenValue())) {
            throw new ValidationException("token", "invalid value");
        }
        user.setName(dto.getUsername());
        user.setSecret(userSecret);
        user.setActivated(true);
        user.setBlocked(false);
        tokenRepository.delete(token);
        userRepository.save(user);
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
