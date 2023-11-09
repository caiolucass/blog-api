package com.solides.blogapi.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank
    @Size(min = 10, message = "Os comentarios devem ter no minimo 10 caracteres.")
    private String body;
}
