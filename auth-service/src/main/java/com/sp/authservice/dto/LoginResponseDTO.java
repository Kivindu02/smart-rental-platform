package com.sp.authservice.dto;

public class LoginResponseDTO {

    private final String token;
    private final String email;
    private final String role;

    public LoginResponseDTO(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
