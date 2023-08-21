package com.example.blogapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCommentDto {
    @NotEmpty(message = "body should not be empty")
    @Size(min = 10, message = "body should have atleast 10 characters")
    private String body;
}
