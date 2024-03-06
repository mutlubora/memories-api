package com.memories.api.auth.token;

import com.memories.api.auth.dto.Credentials;
import com.memories.api.user.User;
import com.memories.api.user.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@ConditionalOnProperty(name = "memories.token-type", havingValue = "basic")
public class BasicAuthTokenService implements TokenService{
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public BasicAuthTokenService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Token createToken(User user, Credentials credentials) {
        String usernameColonPassword = credentials.username() + ":" + credentials.password();
        String token = Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());
        return new Token("Basic", token);
    }

    @Override
    public User verifyToken(String authorizationHeader) {
        return null;
    }
}
