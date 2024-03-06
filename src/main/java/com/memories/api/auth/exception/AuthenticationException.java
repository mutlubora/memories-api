package com.memories.api.auth.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Invalid username or password.");
    }
}
