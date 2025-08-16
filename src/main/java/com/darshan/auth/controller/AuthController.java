package com.darshan.auth.controller;

import com.darshan.auth.entity.User;
import com.darshan.auth.enums.Role;
import com.darshan.auth.repository.UserRepository;
import com.darshan.auth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ---------------- Register ----------------
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already exists!";
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Optional: default role and enable
        if (user.getRole() == null) user.setRole(Role.USER);
        user.setEnabled(true);

        userRepository.save(user);
        return "User registered successfully!";
    }

    // ---------------- Authenticate / Login ----------------
    @PostMapping("/login")
    public String login(@RequestBody User loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new Exception("Invalid username or password", e);
        }

        // Generate JWT token
        return jwtUtil.generateToken(loginRequest.getUsername());
    }
}
