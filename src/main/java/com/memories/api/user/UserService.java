package com.memories.api.user;

import com.memories.api.email.EmailService;
import com.memories.api.file.FileService;
import com.memories.api.memory.Memory;
import com.memories.api.user.dto.UserUpdateRequest;
import com.memories.api.user.exception.InvalidTokenException;
import com.memories.api.user.exception.NotUniqueUsernameException;
import com.memories.api.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final FileService fileService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, FileService fileService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.fileService = fileService;
    }


    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new NotUniqueUsernameException();
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setActivationToken(UUID.randomUUID().toString());

        emailService.sendActivationEmail(user.getEmail(), user.getActivationToken());
        return userRepository.save(user);
    }

    public void activateUser(String token) {
        User userInDB = userRepository.findByActivationToken(token);

        if (userInDB == null) {
            throw new InvalidTokenException();
        }

        userInDB.setActive(true);
        userInDB.setActivationToken(null);

        userRepository.save(userInDB);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User updateUser(Long id, UserUpdateRequest userUpdate) {
        User userInDB = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (!userUpdate.username().equals(userInDB.getUsername())) {
            if (userRepository.findByUsername(userUpdate.username()) != null) {
                throw new NotUniqueUsernameException();
            }
        }

        userInDB.setUsername(userUpdate.username());
        if (userUpdate.image() != null) {
            String fileName = fileService.saveBas64StringAsFile(userUpdate.image());
            fileService.deleteProfileImage(userInDB.getImage());
            userInDB.setImage(fileName);
        }
        return userRepository.save(userInDB);
    }


    public void deleteUser(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            return;
        }
        User user = userRepository.findById(id).get();
        fileService.deleteAllStoredFilesOfUser(user);

        userRepository.deleteById(id);
    }
}
