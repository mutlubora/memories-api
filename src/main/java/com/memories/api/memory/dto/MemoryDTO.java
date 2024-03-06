package com.memories.api.memory.dto;

import com.memories.api.file.FileAttachmentDTO;
import com.memories.api.memory.Memory;
import com.memories.api.user.dto.UserDTO;

public class MemoryDTO {
    private long id;
    private String content;
    private long timestamp;
    private UserDTO user;
    private FileAttachmentDTO fileAttachment;

    public MemoryDTO(Memory memory) {
        this.id = memory.getId();
        this.content = memory.getContent();
        this.timestamp = memory.getTimestamp().getTime();
        this.user = new UserDTO(memory.getUser());
        if (memory.getFileAttachment() != null) {
            this.fileAttachment = new FileAttachmentDTO(memory.getFileAttachment());
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public FileAttachmentDTO getFileAttachment() {
        return fileAttachment;
    }

    public void setFileAttachment(FileAttachmentDTO fileAttachment) {
        this.fileAttachment = fileAttachment;
    }
}
