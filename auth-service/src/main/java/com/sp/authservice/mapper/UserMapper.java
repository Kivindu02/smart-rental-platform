package com.sp.authservice.mapper;

import com.sp.authservice.dto.RegisterRequestDTO;
import com.sp.authservice.dto.UserResponseDTO;
import com.sp.authservice.enums.Role;
import com.sp.authservice.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toModel(RegisterRequestDTO registerRequestDTO) {
        User user = new User();
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPhoneNo(registerRequestDTO.getPhoneNo());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(Role.USER);
        return user;

    }

    public UserResponseDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNo(user.getPhoneNo());
        dto.setRole(user.getRole().name());
        dto.setActive(user.getActive());
        return dto;
    }
}
