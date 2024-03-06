package com.memories.api.auth;

import com.memories.api.auth.dto.AuthResponse;
import com.memories.api.auth.dto.Credentials;
import com.memories.api.auth.exception.AuthenticationException;
import com.memories.api.auth.token.Token;
import com.memories.api.auth.token.TokenService;
import com.memories.api.user.User;
import com.memories.api.user.UserService;
import com.memories.api.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    public AuthService(UserService userService, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }
    public AuthResponse authenticate(Credentials credentials) {
        User userInDB = userService.findByUsername(credentials.username());
        if (userInDB == null) {
            throw new AuthenticationException();
        }

        boolean matches = passwordEncoder.matches(credentials.password(), userInDB.getPassword());
        if (!matches) {
            throw new AuthenticationException();
        }
        Token token = tokenService.createToken(userInDB, credentials);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setUser(new UserDTO(userInDB));

        return authResponse;
    }
}
