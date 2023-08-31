package com.example.blogapp.service.impl;

import com.example.blogapp.exception.ApiException;
import com.example.blogapp.model.Role;
import com.example.blogapp.model.User;
import com.example.blogapp.repository.RoleRepository;
import com.example.blogapp.repository.UserRepository;
import com.example.blogapp.security.JwtTokenProvider;
import com.example.blogapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Override
    public String login(String emailOrUsername, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailOrUsername, password));

        String token = jwtTokenProvider.generateToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return token;
    }

    @Override
    public String register(String name, String username, String email, String password) {
        //check if email already exists, if exists then throw exception
        if(userRepository.existsUserByEmail(email)){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email already exists.");
        }
        //check if username already exists, if exists then throw exception
        if(userRepository.existsUserByUsername(username)){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Username already exists.");
        }

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_USER").get();     // default role to every user
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);  //save the user to db
        return "User registered successfully!";
    }
}
