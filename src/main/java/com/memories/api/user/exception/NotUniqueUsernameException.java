package com.memories.api.user.exception;

public class NotUniqueUsernameException extends RuntimeException{

    public NotUniqueUsernameException() {
        super("This username is already in use.");
    }
}
