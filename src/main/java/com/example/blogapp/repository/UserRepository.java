package com.example.blogapp.repository;

import com.example.blogapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrUsername(String email, String username);
    Optional<User> findByUsername(String username);
    Boolean existsUserByEmail(String email);
    Boolean existsUserByUsername(String username);
}
