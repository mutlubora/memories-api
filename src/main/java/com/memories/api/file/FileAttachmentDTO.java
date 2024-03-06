package com.memories.api.file;

public class FileAttachmentDTO {
    private String name;

    public FileAttachmentDTO(FileAttachment fileAttachment) {
        this.name = fileAttachment.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
