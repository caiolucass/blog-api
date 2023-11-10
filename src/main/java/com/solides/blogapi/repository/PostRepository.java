package com.solides.blogapi.repository;

import com.solides.blogapi.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCreatedBy(Long userId, Pageable pageable);

    Long countByCreatedBy(Long userId);
}
