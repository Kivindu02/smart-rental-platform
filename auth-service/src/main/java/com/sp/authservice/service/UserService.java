package com.sp.authservice.service;

import com.sp.authservice.dto.LoginRequestDTO;
import com.sp.authservice.dto.LoginResponseDTO;
import com.sp.authservice.dto.RegisterRequestDTO;
import com.sp.authservice.exception.*;
import com.sp.authservice.kafka.UserKafkaProducer;
import com.sp.authservice.mapper.UserMapper;
import com.sp.authservice.model.User;
import com.sp.authservice.repository.UserRepository;
import com.sp.authservice.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final UserKafkaProducer userKafkaProducer;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       EmailService emailService,
                       UserKafkaProducer userKafkaProducer) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.userKafkaProducer = userKafkaProducer;
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

    @Transactional
    public void deleteUser(UUID id, UUID loggedInUserId, boolean isAdmin) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        validateAccess(user, loggedInUserId, isAdmin);

        userRepository.delete(user);

        try {
            userKafkaProducer.sendUserDeleteEvent(id);
            log.info("User {} deleted and USER_DELETED event published", id);
        } catch (Exception e) {
            log.error("USER_DELETED Kafka event failed for userId {}: {}", id, e.getMessage());
        }

    }

    private void validateAccess(User user, UUID loggedInUserId, boolean isAdmin) {
        // Allow if user is Admin OR if user is the Owner
        if (isAdmin || user.getId().equals(loggedInUserId)) {
            return; // Access granted
        }

        log.error("Unauthorized access attempt by user {}", loggedInUserId);
        throw new AccessDeniedException("Access Denied: You do not have permission to delete this.");
    }
}
