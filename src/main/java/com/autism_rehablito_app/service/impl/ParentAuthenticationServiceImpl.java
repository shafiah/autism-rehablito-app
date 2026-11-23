package com.autism_rehablito_app.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autism_rehablito_app.dto.parent.ParentForgotPasswordRequest;
import com.autism_rehablito_app.dto.parent.ParentForgotPasswordResponse;
import com.autism_rehablito_app.dto.parent.ParentLoginRequest;
import com.autism_rehablito_app.dto.parent.ParentLoginResponse;
import com.autism_rehablito_app.dto.parent.ParentRegisterRequest;
import com.autism_rehablito_app.dto.parent.ParentRegisterResponse;
import com.autism_rehablito_app.dto.parent.ParentResetPasswordRequest;
import com.autism_rehablito_app.dto.parent.ParentVerifyOtpRequest;
import com.autism_rehablito_app.dto.parent.ParentVerifyOtpResponse;
import com.autism_rehablito_app.entity.Branch;
import com.autism_rehablito_app.entity.NotificationType;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.entity.PasswordResetOtp;
import com.autism_rehablito_app.entity.PasswordResetToken;
import com.autism_rehablito_app.entity.Role;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.exception.UnauthorizedException;
import com.autism_rehablito_app.repository.BranchRepository;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.repository.PasswordResetOtpRepository;
import com.autism_rehablito_app.repository.PasswordResetTokenRepository;
import com.autism_rehablito_app.repository.UserRepository;
import com.autism_rehablito_app.service.JwtService;
import com.autism_rehablito_app.service.NotificationEventService;
import com.autism_rehablito_app.service.ParentAuthenticationService;

@Service
public class ParentAuthenticationServiceImpl
        implements ParentAuthenticationService {

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int MAX_OTP_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final BranchRepository branchRepository;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final NotificationEventService notificationEventService;
    

    private final SecureRandom secureRandom = new SecureRandom();

    public ParentAuthenticationServiceImpl(
            UserRepository userRepository,
            ParentRepository parentRepository,
            BranchRepository branchRepository,
            PasswordResetOtpRepository passwordResetOtpRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            NotificationEventService notificationEventService) {

        this.userRepository = userRepository;
        this.parentRepository = parentRepository;
        this.branchRepository = branchRepository;
        this.passwordResetOtpRepository =
                passwordResetOtpRepository;
        this.passwordResetTokenRepository =
                passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.notificationEventService = notificationEventService;
    }

    @Override
    @Transactional
    public ParentRegisterResponse register(
            ParentRegisterRequest request) {

        String email = request.getEmail().trim();
        String mobileNumber = request.getMobileNumber().trim();

        // Check duplicate email
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        // Check duplicate mobile number
        if (userRepository.existsByMobileNumber(mobileNumber)) {
            throw new IllegalArgumentException(
                    "Mobile number is already registered"
            );
        }

        // Validate selected branch
        Branch branch = branchRepository
                .findById(request.getBranchId())
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Selected branch not found"
                        )
                );

        // Check branch active status
        if (!Boolean.TRUE.equals(branch.getActive())) {
            throw new IllegalArgumentException(
                    "Selected branch is inactive"
            );
        }

        // Create User
        User user = new User();

        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setMobileNumber(mobileNumber);

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(Role.PARENT);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        // Create Parent profile
        Parent parent = new Parent();

        parent.setUser(savedUser);
        parent.setBranch(branch);
        parent.setOccupation(request.getOccupation());
        parent.setAddress(request.getAddress());
        parent.setActive(true);

        Parent savedParent = parentRepository.save(parent);
        
        notificationEventService.notifyAdmins(
                savedParent.getBranch().getId(),
                "New Parent Registered",
                "New parent registered: "
                        + savedUser.getName()
                        + " (" + savedUser.getEmail() + ")",
                NotificationType.SYSTEM,
                "PARENT",
                savedParent.getId()
        );

        return new ParentRegisterResponse(
                savedUser.getId(),
                savedParent.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getMobileNumber(),
                savedUser.getRole().name(),
                "Parent registered successfully"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ParentLoginResponse login(
            ParentLoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail().trim())
                .orElseThrow(
                        () -> new UnauthorizedException(
                                "Invalid email or password"
                        )
                );

        if (user.getRole() != Role.PARENT) {
            throw new UnauthorizedException(
                    "Parent access is not allowed"
            );
        }

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UnauthorizedException(
                    "Parent account is inactive"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new UnauthorizedException(
                    "Invalid email or password"
            );
        }

        Parent parent = parentRepository
                .findByUserId(user.getId())
                .orElseThrow(
                        () -> new UnauthorizedException(
                                "Parent profile not found"
                        )
                );

        String token =
                jwtService.generateToken(user);

        return new ParentLoginResponse(
                user.getId(),
                parent.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                token
        );
    }

    @Override
    @Transactional
    public ParentForgotPasswordResponse forgotPassword(
            ParentForgotPasswordRequest request) {

        String email = request.getEmail().trim();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Parent account not found"
                        )
                );

        if (user.getRole() != Role.PARENT) {
            throw new IllegalArgumentException(
                    "Parent account not found"
            );
        }

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new IllegalArgumentException(
                    "Parent account is inactive"
            );
        }

        PasswordResetOtp otpEntity =
                passwordResetOtpRepository
                        .findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(
                                user.getId()
                        )
                        .orElse(null);

        if (otpEntity != null
                && otpEntity.getExpiresAt()
                .isAfter(LocalDateTime.now())) {

            otpEntity.setUsed(true);

            passwordResetOtpRepository.save(
                    otpEntity
            );
        }

        String otp = generateOtp();

        PasswordResetOtp newOtp =
                new PasswordResetOtp();

        newOtp.setUser(user);

        newOtp.setOtpHash(
                passwordEncoder.encode(otp)
        );

        newOtp.setExpiresAt(
                LocalDateTime.now()
                        .plusMinutes(OTP_EXPIRY_MINUTES)
        );

        newOtp.setAttempts(0);
        newOtp.setVerified(false);
        newOtp.setUsed(false);

        passwordResetOtpRepository.save(newOtp);

        /*
         * Development only.
         *
         * Production mein OTP yahan se SMS/Email
         * provider ko bhejna hai.
         */
        System.out.println(
                "========================================"
        );
        System.out.println(
                "PASSWORD RESET OTP - DEVELOPMENT ONLY"
        );
        System.out.println(
                "Email: " + email
        );
        System.out.println(
                "OTP: " + otp
        );
        System.out.println(
                "Expires in: "
                        + OTP_EXPIRY_MINUTES
                        + " minutes"
        );
        System.out.println(
                "========================================"
        );

        return new ParentForgotPasswordResponse(
                "OTP sent successfully"
        );
    }

    @Override
    @Transactional
    public ParentVerifyOtpResponse verifyOtp(
            ParentVerifyOtpRequest request) {

        String email = request.getEmail().trim();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Invalid OTP request"
                        )
                );

        if (user.getRole() != Role.PARENT) {
            throw new IllegalArgumentException(
                    "Invalid OTP request"
            );
        }

        PasswordResetOtp otpEntity =
                passwordResetOtpRepository
                        .findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(
                                user.getId()
                        )
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "OTP not found or expired"
                                )
                        );

        if (otpEntity.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            otpEntity.setUsed(true);

            passwordResetOtpRepository.save(
                    otpEntity
            );

            throw new IllegalArgumentException(
                    "OTP has expired"
            );
        }

        if (otpEntity.getAttempts()
                >= MAX_OTP_ATTEMPTS) {

            otpEntity.setUsed(true);

            passwordResetOtpRepository.save(
                    otpEntity
            );

            throw new IllegalArgumentException(
                    "Maximum OTP attempts exceeded"
            );
        }

        if (!passwordEncoder.matches(
                request.getOtp(),
                otpEntity.getOtpHash())) {

            otpEntity.setAttempts(
                    otpEntity.getAttempts() + 1
            );

            passwordResetOtpRepository.save(
                    otpEntity
            );

            throw new IllegalArgumentException(
                    "Invalid OTP"
            );
        }

        otpEntity.setVerified(true);
        otpEntity.setUsed(true);

        passwordResetOtpRepository.save(
                otpEntity
        );

        String resetToken =
                UUID.randomUUID().toString();

        PasswordResetToken resetTokenEntity =
                new PasswordResetToken();

        resetTokenEntity.setUser(user);

        resetTokenEntity.setTokenHash(
                hashValue(resetToken)
        );

        resetTokenEntity.setExpiresAt(
                LocalDateTime.now()
                        .plusMinutes(10)
        );

        resetTokenEntity.setUsed(false);

        passwordResetTokenRepository.save(
                resetTokenEntity
        );

        return new ParentVerifyOtpResponse(
                "OTP verified successfully",
                resetToken
        );
    }

    @Override
    @Transactional
    public void resetPassword(
            ParentResetPasswordRequest request) {

        String tokenHash =
                hashValue(request.getResetToken());

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByTokenHashAndUsedFalse(
                                tokenHash
                        )
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Invalid or expired reset token"
                                )
                        );

        if (resetToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            resetToken.setUsed(true);

            passwordResetTokenRepository.save(
                    resetToken
            );

            throw new IllegalArgumentException(
                    "Reset token has expired"
            );
        }

        User user = resetToken.getUser();

        if (user.getRole() != Role.PARENT) {
            throw new IllegalArgumentException(
                    "Invalid password reset request"
            );
        }

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new IllegalArgumentException(
                    "Parent account is inactive"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        resetToken.setUsed(true);

        passwordResetTokenRepository.save(
                resetToken
        );
    }

    private String generateOtp() {

        int otpNumber =
                secureRandom.nextInt(1_000_000);

        return String.format(
                "%06d",
                otpNumber
        );
    }

    private String hashValue(String value) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            value.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            return HexFormat.of()
                    .formatHex(hash);

        } catch (NoSuchAlgorithmException exception) {

            throw new IllegalStateException(
                    "Unable to generate secure hash"
            );
        }
    }
}