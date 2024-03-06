package com.memories.api.user;

import com.memories.api.user.dto.UserCreateRequest;
import com.memories.api.user.dto.UserDTO;
import com.memories.api.user.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.createUser(userCreateRequest.toUser()));
    }

    @PatchMapping("/active")
    public ResponseEntity<?> activateUser(@RequestParam String token) {
        userService.activateUser(token);
        return ResponseEntity.ok("User activated successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @Validated @RequestBody UserUpdateRequest userUpdate) {
        User updatedUser = userService.updateUser(id,userUpdate);
        return ResponseEntity.ok(new UserDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}






























