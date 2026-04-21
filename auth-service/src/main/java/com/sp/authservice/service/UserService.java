package com.sp.authservice.service;

import com.sp.authservice.dto.RegisterRequestDTO;
import com.sp.authservice.exception.EmailAlreadyExistException;
import com.sp.authservice.exception.PasswordMismatchException;
import com.sp.authservice.mapper.UserMapper;
import com.sp.authservice.model.User;
import com.sp.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
}
