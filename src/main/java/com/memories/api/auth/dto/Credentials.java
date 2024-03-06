package com.memories.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record Credentials(@NotBlank(message = "Username can not be empty.") String username,
                          @NotBlank(message = "Password can not be empty.") String password) {

}
