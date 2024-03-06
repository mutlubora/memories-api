package com.memories.api.user.dto;

import com.memories.api.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreate(
        @NotBlank(message = "Username can not be empty.")
        @Size(min = 4, max = 255, message = "Username must be between 4-255 characters.")
        String username,
        @NotBlank(message = "Email can not be empty.")
        @Email
        String email,
        @Size(min = 8, max = 255, message = "Password must be between 8-255 characters.")
        @NotBlank(message = "Password can not be empty.")
        String password
) {
    public User toUser() {
        User user = new User();
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setPassword(this.password);
        return user;
    }
}
