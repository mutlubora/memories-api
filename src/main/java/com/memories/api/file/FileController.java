package com.memories.api.file;

import com.memories.api.user.validation.FileType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/api/v1/memory-attachments")
    FileAttachment saveMemoryAttachment(MultipartFile file) {
        return fileService.saveMemoryAttachment(file);
    }
}
