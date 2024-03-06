package com.memories.api.auth;

import com.memories.api.auth.dto.Credentials;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping
    public ResponseEntity<?> handleAuthentication(@RequestBody @Valid Credentials credentials) {
        return ResponseEntity.ok(authService.authenticate(credentials));
    }
}
