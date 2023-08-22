package com.example.blogapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequestDto {
    @NotEmpty
    @Size(min = 3, message = "name should have atleast 3 characters")
    private String name;
    @NotEmpty
    @Size(min = 4, message = "Username should have atleast 4 characters")
    private String username;
    @Email
    @NotEmpty
    private String email;
    @NotBlank
    @Size(min = 4, message = "Password should have atleast 4 characters")
    private String password;
}
