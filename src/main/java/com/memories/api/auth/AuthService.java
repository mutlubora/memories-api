package com.memories.api.auth.dto;

import com.memories.api.auth.exception.AuthenticationException;
import com.memories.api.user.User;
import com.memories.api.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public AuthService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    public void authenticate(Credentials credentials) {
        User userInDB = userService.findByUsername(credentials.username());
        if (userInDB == null) {
            throw new AuthenticationException();
        }

        boolean matches = passwordEncoder.matches(credentials.password(), userInDB.getPassword());
        if (!matches) {
            throw new AuthenticationException();
        }

    }
}
