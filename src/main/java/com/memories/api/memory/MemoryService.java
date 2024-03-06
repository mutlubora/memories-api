package com.memories.api.memory;

import com.memories.api.auth.exception.AuthorizationException;
import com.memories.api.config.CurrentUser;
import com.memories.api.file.FileAttachment;
import com.memories.api.file.FileAttachmentRepository;
import com.memories.api.file.FileService;
import com.memories.api.memory.dto.MemorySubmitRequest;
import com.memories.api.user.User;
import com.memories.api.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MemoryService {
    private final MemoryRepository memoryRepository;
    private final UserService userService;
    private final FileAttachmentRepository fileAttachmentRepository;
    private final FileService fileService;
    public MemoryService(MemoryRepository memoryRepository, UserService userService, FileAttachmentRepository fileAttachmentRepository, FileService fileService) {
        this.memoryRepository = memoryRepository;
        this.userService = userService;
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.fileService = fileService;
    }

    public Memory saveMemory(MemorySubmitRequest dto, Long id) {
        User user = userService.getUserById(id);
        Memory memory = new Memory();
        memory.setTimestamp(new Date());
        memory.setContent(dto.content());
        memory.setUser(user);

        Optional<FileAttachment> optionalFileAttachment = fileAttachmentRepository.findById(dto.attachmentId());
        if (optionalFileAttachment.isPresent()) {
            FileAttachment fileAttachment = optionalFileAttachment.get();
            fileAttachment.setMemory(memory);
        }
        return memoryRepository.save(memory);
    }

    public Page<Memory> getMemories(Pageable pageable) {
        return memoryRepository.findAll(pageable);
    }

    public Page<Memory> getMemoriesOfUser(Long userId, Pageable pageable) {
        User user = userService.getUserById(userId);
        return memoryRepository.findByUser(user, pageable);
    }

    public Page<Memory> getOldMemories(Long id, Pageable pageable) {
        return memoryRepository.findByIdLessThan(id, pageable);
    }

    public Page<Memory> getOldMemoriesOfUser(Long id, Long userId, Pageable pageable) {
        User user = userService.getUserById(userId);
        return memoryRepository.findByIdLessThanAndUser(id, user, pageable);
    }

    public long getNewMemoriesCount(Long id) {
        return memoryRepository.countByIdGreaterThan(id);
    }

    public long getNewMemoriesCountOfUser(Long id, Long userId) {
        User user = userService.getUserById(userId);
        return memoryRepository.countByIdGreaterThanAndUser(id, user);
    }

    public List<Memory> getNewMemories(Long id) {
        return memoryRepository.findByIdGreaterThanOrderByIdDesc(id);
    }

    public List<Memory> getNewMemoriesOfUser(Long id, Long userId) {
        User user = userService.getUserById(userId);
        return memoryRepository.findByIdGreaterThanAndUserOrderByIdDesc(id, user);
    }

    public void delete(Long id, CurrentUser loggedInUser) {
        Optional<Memory> optionalMemory = memoryRepository.findById(id);
        if (optionalMemory.isEmpty()) {
            return;
        }
        Memory memory = optionalMemory.get();
        if (!Objects.equals(memory.getUser().getId(), loggedInUser.getId())) {
            throw new AuthorizationException();
        }

        if (memory.getFileAttachment() != null) {
            String imageName = memory.getFileAttachment().getName();
            fileService.deleteAttachmentFile(imageName);

        }
        memoryRepository.deleteById(id);
    }
}
