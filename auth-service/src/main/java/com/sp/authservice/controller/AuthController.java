package com.sp.authservice.controller;

import com.sp.authservice.dto.LoginRequestDTO;
import com.sp.authservice.dto.LoginResponseDTO;
import com.sp.authservice.dto.RegisterRequestDTO;
import com.sp.authservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        userService.register(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO responseDTO = userService.login(loginRequestDTO);
        return  ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/users/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deactivateUser(@PathVariable UUID id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok("User deactivated successfully");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String  token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully! You can now login.");
    }
}
