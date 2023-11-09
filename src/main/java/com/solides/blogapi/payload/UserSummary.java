package com.solides.blogapi.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserSummary {
    private Long id;
    private String username;
}
