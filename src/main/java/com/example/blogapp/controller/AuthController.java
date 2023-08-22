package com.example.blogapp.controller;

import com.example.blogapp.dto.LoginRequestDto;
import com.example.blogapp.dto.RegisterUserRequestDto;
import com.example.blogapp.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog/auth")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping(value = {"/login", "signin"})
    public ResponseEntity<String> login(@RequestBody LoginRequestDto request){
        String response = authService.login(request.getEmailOrUsername(), request.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = {"/register", "signup"})
    public ResponseEntity<String> register(@RequestBody RegisterUserRequestDto request){
        String response = authService.register(request.getName(),
                request.getUsername(), request.getEmail(), request.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
