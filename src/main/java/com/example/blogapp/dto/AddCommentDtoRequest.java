package com.example.blogapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddCommentDtoRequest {
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, message = "Name should have atleast 2 characters")
    private String name;
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
    @NotEmpty(message = "Body should not be empty")
    @Size(min = 10, message = "Body should have atleast 10 characters")
    private String body;
}
