package com.cognivanta.user_service.service.implementation;

import com.cognivanta.user_service.domain.dto.LoginUserDto;
import com.cognivanta.user_service.domain.dto.RegisterUserDto;
import com.cognivanta.user_service.domain.dto.VerifyUserDto;
import com.cognivanta.user_service.domain.entity.User;
import com.cognivanta.user_service.exception.UserAlreadyExistException;
import com.cognivanta.user_service.repository.UserRepository;
import com.cognivanta.user_service.service.AuthenticationService;
import com.cognivanta.user_service.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepo;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public User signup(RegisterUserDto request) {

        if(userRepo.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistException("User with email " + request.getEmail() + " already exists");

        if(userRepo.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistException("User with username " + request.getUsername() + " already exists");


        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .number(request.getNumber())
                .password(encoder.encode(request.getPassword()))
                .verificationCode(generateVerificationCode())
                .verificationCodeExpiration(LocalDateTime.now().plusMinutes(15))
                .build();

        User savedUser = userRepo.save(user);

        sendVerificationEmail(user);

        return savedUser;
    }

    @Override
    public UserDetails authenticate(LoginUserDto request) {
        User existingUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with the follwing email: "));

        if(!existingUser.isEnabled()) {
            throw new RuntimeException("Account not verified yet. Please verify you account");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return userDetailsService.loadUserByUsername(request.getEmail());
    }

    @Override
    public void verifyUser(VerifyUserDto request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getVerificationCodeExpiration().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has been expired");
            }
            if (user.getVerificationCode().equals(request.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiration(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void resenVerificationEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if(user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiration(LocalDateTime.now().plusMinutes(15));
            userRepository.save(user);
            sendVerificationEmail(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void sendVerificationEmail(User user) {
    String subject = "Verify Your Email - Ecom® Online Shopping";
    String verificationCode = user.getVerificationCode();

    String htmlMessage = "<html>" +
            "<body style='font-family: Arial, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0;'>" +
            "<div style='max-width: 600px; margin: auto; padding: 30px; background-color: #ffffff; border-radius: 8px; " +
            "box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>" +

            "<h2 style='color: #2a7ae2; text-align: center;'>🛍️ Welcome to Seasons®</h2>" +
            "<p style='font-size: 16px; text-align: center; color: #555;'>Thank you for signing up! To complete your registration, please verify your email address using the code below:</p>" +

            "<div style='margin: 30px auto; padding: 20px; background-color: #f3f8ff; border-left: 5px solid #2a7ae2; " +
            "border-radius: 5px; text-align: center;'>" +
            "<h3 style='margin: 0; color: #333;'>Your Verification Code:</h3>" +
            "<p style='font-size: 26px; font-weight: bold; color: #2a7ae2; margin-top: 10px; letter-spacing: 2px;'>" + verificationCode + "</p>" +
            "</div>" +

            "<p style='font-size: 14px; color: #777; text-align: center;'>If you did not request this, you can safely ignore this email.</p>" +
            "<p style='font-size: 14px; color: #777; text-align: center;'>Happy Shopping! <br/>– The Seasons® Team</p>" +
            "</div>" +
            "</body>" +
            "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(100000) + 900000;
        return String.valueOf(code);
    }
}
