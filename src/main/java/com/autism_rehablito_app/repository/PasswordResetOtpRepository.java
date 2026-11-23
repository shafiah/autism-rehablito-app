package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository
        extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp>
    findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(Long userId);
}