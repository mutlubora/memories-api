package com.memories.api.user.dto;

import com.memories.api.user.validation.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(@NotBlank(message = "Username can not be empty.")
                                @Size(min = 4, max = 255, message = "Username must be between 4-255 characters.")
                                String username,
                                @FileType(types = {"jpeg", "png"})
                                String image){
}
