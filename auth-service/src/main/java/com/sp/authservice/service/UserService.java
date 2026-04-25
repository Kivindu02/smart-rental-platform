package com.sp.authservice.service;

import com.sp.authservice.dto.LoginRequestDTO;
import com.sp.authservice.dto.LoginResponseDTO;
import com.sp.authservice.dto.RegisterRequestDTO;
import com.sp.authservice.exception.*;
import com.sp.authservice.mapper.UserMapper;
import com.sp.authservice.model.User;
import com.sp.authservice.repository.UserRepository;
import com.sp.authservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequestDTO registerRequestDTO) {
        if(!registerRequestDTO.getPassword().equals(registerRequestDTO.getConfirmPassword())) {
            throw new PasswordMismatchException("Password do not match");
        }

        if(userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            throw  new EmailAlreadyExistException("Email already registered");
        }

        User user = userMapper.toModel(registerRequestDTO);
        userRepository.save(user);

    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() -> new UserNotFoundException("User not Found"));

        if(!user.getActive()) {
            throw new AccountDeactivatedException("Your Account has been deactivated. Please contact support");
        }

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credential");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId().toString()
        );

        return new LoginResponseDTO(token, user.getEmail(), user.getRole().name());

    }

    public void deactivateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not found"));
        user.setActive(false);
        userRepository.save(user);
    }
}
