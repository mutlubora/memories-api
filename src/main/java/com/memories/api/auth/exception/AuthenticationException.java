package com.memories.api.auth;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Invalid username or password.");
    }
}
