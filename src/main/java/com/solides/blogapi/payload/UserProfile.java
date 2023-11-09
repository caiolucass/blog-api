package com.solides.blogapi.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private Long id;
    private String username;
    private Instant joinedAt;
    private String email;
    private Long postCount;

    public UserProfile(Long id, String username, Long postCount) {
    }
}
