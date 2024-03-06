package com.memories.api.memory;

import com.memories.api.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
    Page<Memory> findByUser(User user, Pageable pageable);

    Page<Memory> findByIdLessThan(Long id, Pageable pageable);

    Page<Memory> findByIdLessThanAndUser(Long id, User user, Pageable pageable);

    long countByIdGreaterThan(Long id);

    long countByIdGreaterThanAndUser(Long id, User user);

    List<Memory> findByIdGreaterThanOrderByIdDesc(Long id);

    List<Memory> findByIdGreaterThanAndUserOrderByIdDesc(Long id, User user);
}
