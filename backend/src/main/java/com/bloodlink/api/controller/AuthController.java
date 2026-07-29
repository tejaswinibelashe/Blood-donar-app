package com.bloodlink.api.controller;

import com.bloodlink.api.entity.User;
import com.bloodlink.api.repository.UserRepository;
import com.bloodlink.api.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, SimpMessagingTemplate messagingTemplate, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messagingTemplate = messagingTemplate;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }
        
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            return ResponseEntity.badRequest().body("Error: Phone number is already in use!");
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Save user
        User savedUser = userRepository.save(user);

        // Broadcast to WebSocket /topic/public
        messagingTemplate.convertAndSend("/topic/public", savedUser);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            // Generate real JWT
            String jwt = jwtUtil.generateToken(userOpt.get().getEmail());
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("user", userOpt.get());
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Error: Invalid email or password");
        }
    }
}
