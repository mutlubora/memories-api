package com.memories.api.user;

import com.memories.api.memory.Memory;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Username can not be empty.")
    @Size(min = 4, max = 255, message = "Username must be between 4-255 characters.")
    private String username;

    @NotBlank(message = "Email can not be empty.")
    @Email
    private String email;
    @Size(min = 8, max = 255, message = "Password must be between 8-255 characters.")
    @NotBlank(message = "Password can not be empty.")
    private String password;
    private String image;
    private boolean active;
    private String activationToken;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Memory> memory;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Memory> getMemory() {
        return memory;
    }

    public void setMemory(List<Memory> memory) {
        this.memory = memory;
    }
}
