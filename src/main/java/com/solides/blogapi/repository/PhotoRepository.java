package com.solides.blogapi.repository;

import com.solides.blogapi.model.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Page<Photo> findByAlbumId(Long albumId, Pageable pageable);
}
