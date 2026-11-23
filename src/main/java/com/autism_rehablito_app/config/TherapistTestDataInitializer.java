//package com.autism_rehablito_app.config;
//
//import com.autism_rehablito_app.entity.Branch;
//import com.autism_rehablito_app.entity.Role;
//import com.autism_rehablito_app.entity.Therapist;
//import com.autism_rehablito_app.entity.User;
//import com.autism_rehablito_app.repository.BranchRepository;
//import com.autism_rehablito_app.repository.TherapistRepository;
//import com.autism_rehablito_app.repository.UserRepository;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class TherapistTestDataInitializer {
//
//    @Bean
//    CommandLineRunner initializeTestTherapist(
//            UserRepository userRepository,
//            TherapistRepository therapistRepository,
//            BranchRepository branchRepository,
//            PasswordEncoder passwordEncoder) {
//
//        return args -> {
//
//            String therapistEmail =
//                    "therapist@gmail.com";
//
//            if (userRepository
//                    .findByEmail(therapistEmail)
//                    .isEmpty()) {
//
//                Branch branch =
//                        branchRepository
//                                .findByBranchName("Patna")
//                                .orElseThrow(
//                                        () -> new IllegalStateException(
//                                                "Patna branch not found"
//                                        )
//                                );
//
//                User therapistUser =
//                        new User();
//
//                therapistUser.setName(
//                        "Test Therapist"
//                );
//
//                therapistUser.setEmail(
//                        therapistEmail
//                );
//
//                therapistUser.setMobileNumber(
//                        "8888888888"
//                );
//
//                therapistUser.setPassword(
//                        passwordEncoder.encode(
//                                "123456"
//                        )
//                );
//
//                therapistUser.setRole(
//                        Role.THERAPIST
//                );
//
//                therapistUser.setActive(true);
//
//                User savedUser =
//                        userRepository.save(
//                                therapistUser
//                        );
//
//                Therapist therapist =
//                        new Therapist();
//
//                therapist.setUser(savedUser);
//                therapist.setBranch(branch);
//
//                therapist.setTherapyType(
//                        "Occupational Therapy"
//                );
//
//                therapist.setAvailability(
//                        "Monday to Saturday"
//                );
//
//                therapist.setActive(true);
//
//                therapistRepository.save(
//                        therapist
//                );
//
//                System.out.println(
//                        "Test Therapist created successfully."
//                );
//            }
//        };
//    }
//}