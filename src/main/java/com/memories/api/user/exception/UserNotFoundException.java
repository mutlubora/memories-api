package com.memories.api.user.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long id) {
        super("No user was found for the given ID: " + id);
    }
}
