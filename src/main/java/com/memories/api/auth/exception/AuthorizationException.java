package com.memories.api.auth.exception;

public class AuthorizationException extends RuntimeException{
    public AuthorizationException() {
        super("You do not have permission to delete this memory.");
    }
}
