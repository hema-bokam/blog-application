package com.example.blogapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostRequestDto {
    @NotEmpty
    @Size(min = 2, message = "Title should have atleast 2 characters")
    private String title;
    @NotEmpty
    @Size(min = 4, message="description should have atleast 4 characters")
    private String description;
    @NotEmpty
    private String content;
}
