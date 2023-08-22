package com.example.blogapp.service;

import com.example.blogapp.model.User;

public interface AuthService {
    String login(String emailOrUsername, String password);
    String register(String name, String username, String email, String password);
}
