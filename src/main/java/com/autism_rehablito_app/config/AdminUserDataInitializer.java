package com.autism_rehablito_app.config;

import com.autism_rehablito_app.entity.Role;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserDataInitializer {

    @Bean
    CommandLineRunner initializeAdminUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String adminEmail = "admin@gmail.com";

            if (userRepository
                    .findByEmail(adminEmail)
                    .isEmpty()) {

                User adminUser = new User();

                adminUser.setName("Admin");
                adminUser.setEmail(adminEmail);
                adminUser.setMobileNumber("9999999999");

                adminUser.setPassword(
                        passwordEncoder.encode("123456")
                );

                adminUser.setRole(Role.ADMIN);
                adminUser.setActive(true);

                userRepository.save(adminUser);

                System.out.println(
                        "Default Admin user created successfully."
                );
            }
        };
    }
}