package com.memories.api.user.exception;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException() {
        super("Token is not valid.");
    }
}
