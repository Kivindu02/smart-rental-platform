package com.sp.authservice.config;

import com.sp.authservice.enums.Role;
import com.sp.authservice.model.User;
import com.sp.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.firstname}")
    private String firstName;

    @Value("${app.admin.lastname}")
    private String lastName;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        boolean adminExist = userRepository.existsByEmail(adminEmail);

        if(!adminExist) {
            User admin = new User();
            admin.setFirstName(firstName);
            admin.setLastName(lastName);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            admin.setVerified(true);
            userRepository.save(admin);
            System.out.println("Admin account created: "+ adminEmail);

        }else {
            System.out.println("Admin already exists - skipping");
        }

    }
}
