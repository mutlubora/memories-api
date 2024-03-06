package com.memories.api.file;

import com.memories.api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
    List<FileAttachment> findByDateBeforeAndMemoryIsNull(Date date);
    List<FileAttachment> findByMemoryUser(User user);
}
