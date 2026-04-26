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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    public void register(RegisterRequestDTO registerRequestDTO) {
        if(!registerRequestDTO.getPassword().equals(registerRequestDTO.getConfirmPassword())) {
            throw new PasswordMismatchException("Password do not match");
        }

        if(userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            throw  new EmailAlreadyExistException("Email already registered");
        }
        String verificationToken = UUID.randomUUID().toString();
        User user = userMapper.toModel(registerRequestDTO);
        user.setVerificationToken(verificationToken);
        user.setTokenExpiresAt(LocalDateTime.now().plusHours(24));

        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() -> new UserNotFoundException("User not Found"));

        if(!user.getActive()) {
            throw new AccountDeactivatedException("Your Account has been deactivated. Please contact support");
        }

        if (!user.getVerified()) {
            throw new EmailNotVerifiedException("Please verify your email before logging in");
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

    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (user.getTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired");
        }

        //mark as verified and clear token
        user.setVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiresAt(null);
        userRepository.save(user);
    }
}
