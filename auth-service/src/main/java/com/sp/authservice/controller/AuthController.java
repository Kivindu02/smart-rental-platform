package com.sp.authservice.controller;

import com.sp.authservice.dto.LoginRequestDTO;
import com.sp.authservice.dto.LoginResponseDTO;
import com.sp.authservice.dto.RegisterRequestDTO;
import com.sp.authservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
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

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        UUID loggedInUserId = getAuthenticatedUserId();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        userService.deleteUser(id, loggedInUserId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    private UUID getAuthenticatedUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getDetails() == null) {
            log.error("Authentication or details (userId) are missing from the security context");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User identity not verified");
        }

        return UUID.fromString(authentication.getDetails().toString());
    }

}
