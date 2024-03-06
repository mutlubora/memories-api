package com.memories.api.memory;

import com.memories.api.file.FileAttachment;
import com.memories.api.user.User;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Memory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String content;

    private Date timestamp;
    @ManyToOne()
    private User user;

    @OneToOne(mappedBy = "memory", cascade = CascadeType.REMOVE)
    private FileAttachment fileAttachment;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FileAttachment getFileAttachment() {
        return fileAttachment;
    }

    public void setFileAttachment(FileAttachment fileAttachment) {
        this.fileAttachment = fileAttachment;
    }
}
