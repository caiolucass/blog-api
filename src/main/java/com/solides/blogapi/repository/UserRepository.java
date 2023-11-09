package com.solides.blogapi.repository;

import com.solides.blogapi.exception.ResourceNotFoundException;
import com.solides.blogapi.model.User;
import com.solides.blogapi.security.UserPrincipal;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(@NotBlank String username);

    Optional<User> findByEmail(@NotBlank String email);

    Boolean existsByUsername(@NotBlank String username);

    Boolean existsByEmail(@NotBlank String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    default User getUser(UserPrincipal currentUser) {
        return getUserByName(currentUser.getUsername());
    }

    default User getUserByName(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}
