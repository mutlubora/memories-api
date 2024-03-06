package com.memories.api.memory.dto;

import com.memories.api.memory.Memory;
import jakarta.validation.constraints.Size;

public record MemorySubmitRequest (@Size(min = 1, max = 280) String content,
                                   long attachmentId){
    public Memory toMemory() {
        Memory memory = new Memory();
        memory.setContent(this.content);
        return memory;
    }
}
