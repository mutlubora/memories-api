package com.memories.api.memory;

import com.memories.api.config.CurrentUser;
import com.memories.api.memory.dto.MemoryDTO;
import com.memories.api.memory.dto.MemorySubmitRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MemoryController {
    private final MemoryService memoryService;

    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    @PostMapping("/memories")
    public ResponseEntity<?> saveMemory(@RequestBody MemorySubmitRequest dto, @AuthenticationPrincipal CurrentUser user) {

        Memory savedMemory = memoryService.saveMemory(dto, user.getId());

        return ResponseEntity.ok( new MemoryDTO(savedMemory));
    }
    @GetMapping("/memories")
    public ResponseEntity<?> getMemories(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                             Pageable pageable) {
        Page<Memory> memories = memoryService.getMemories(pageable);

        return ResponseEntity.ok(memories.map(MemoryDTO::new));
    }

    @GetMapping("/memories/{id:[0-9]+}")
    public ResponseEntity<?> getMemoriesRelative(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                                 Pageable pageable, @PathVariable("id") Long id,
                                                 @RequestParam(name = "count", required = false, defaultValue = "false")
                                                 Boolean count, @RequestParam(name = "direction", required = false, defaultValue = "before") String direction) {
        if (count) {
            long newMemoriesCount = memoryService.getNewMemoriesCount(id);
            Map<String, Long> response = new HashMap<>();
            response.put("count", newMemoriesCount);
            return  ResponseEntity.ok(response);
        }

        if (direction.equals("after")) {
            List<MemoryDTO> memories = memoryService.getNewMemories(id).stream()
                                                    .map(MemoryDTO::new).toList();
            return ResponseEntity.ok(memories);
        }

        Page<Memory> memories = memoryService.getOldMemories(id, pageable);
        return ResponseEntity.ok(memories.map(MemoryDTO::new));
    }
    @GetMapping("/users/{userId}/memories")
    public ResponseEntity<?> getUserMemories(@PathVariable Long userId,
                                             @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                         Pageable pageable) {

        Page<Memory> memories = memoryService.getMemoriesOfUser(userId, pageable);

        return ResponseEntity.ok(memories.map(MemoryDTO::new));
    }

    @GetMapping("/users/{userId}/memories/{id:[0-9]+}")
    public ResponseEntity<?> getUserMemoriesRelative(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                                         Pageable pageable, @PathVariable("userId") Long userId,
                                                         @PathVariable("id") Long id,
                                                         @RequestParam(name = "count", required = false, defaultValue = "false") Boolean count,
                                                         @RequestParam(name = "direction", required = false, defaultValue = "before") String direction) {
        if (count) {
            long newMemoriesCount = memoryService.getNewMemoriesCountOfUser(id, userId);
            Map<String, Long> response = new HashMap<>();
            response.put("count", newMemoriesCount);
            return  ResponseEntity.ok(response);
        }

        if (direction.equals("after")) {
            List<MemoryDTO> memories = memoryService.getNewMemoriesOfUser(id, userId).stream()
                    .map(MemoryDTO::new).toList();
            return ResponseEntity.ok(memories);
        }

        Page<Memory> memories = memoryService.getOldMemoriesOfUser(id, userId, pageable);
        return ResponseEntity.ok(memories.map(MemoryDTO::new));
    }

    @DeleteMapping("/memories/{id:[0-9]+}")
    public ResponseEntity<?> deleteMemory(@PathVariable(name = "id") Long id,
                                          @AuthenticationPrincipal CurrentUser user) {

        memoryService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}