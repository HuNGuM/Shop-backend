package com.hungum.shop.service;

import com.hungum.shop.dto.AuthenticationResponse;
import com.hungum.shop.dto.LoginRequestDto;
import com.hungum.shop.dto.RegisterRequestDto;
import com.hungum.shop.exceptions.ApiResponse;
import com.hungum.shop.exceptions.ShopException;
import com.hungum.shop.model.User;
import com.hungum.shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MailContentBuilder mailContentBuilder;
    @Autowired
    private MailService mailService;
    @Autowired
    private TokenService tokenService;
    @Value("${account.verification.url}")
    private String accountVerificationUrl;

    public boolean existsByUserName(RegisterRequestDto registerRequestDto) {
        return userRepository.existsByUsername(registerRequestDto.getUsername());
    }

    public void createUser(RegisterRequestDto registerRequestDto) {
        String encodedPassword = passwordEncoder.encode(registerRequestDto.getPassword());

        User user = new User(registerRequestDto.getEmail(),
                registerRequestDto.getUsername(),
                encodedPassword);
        user.setEnabled(false);
        userRepository.save(user);
        log.info("Saved User to Database, sending activation email");

        String token = generateTokenService(user);
        String message = mailContentBuilder.build("Thank you for signing up to Shop, please click on the below url to activate your account : "
                + accountVerificationUrl + "/" + token);

        mailService.sendMail(user.getEmail(), message);
        log.info("Activation email sent!!");
    }

    private String generateTokenService(User user) {
        String token = UUID.randomUUID().toString();
        tokenService.saveVerificationToken(token, user.getUsername(), Duration.ofHours(24));

        return token;
    }

    public AuthenticationResponse authenticate(LoginRequestDto loginRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String username = loginRequestDto.getUsername();
        String accessToken = jwtTokenProvider.generateToken(authenticate);
        String refreshToken = jwtTokenProvider.generateRefreshToken(username, Duration.ofDays(7).toMillis());

        tokenService.saveRefreshToken(username, refreshToken, Duration.ofDays(7));

        return new AuthenticationResponse(accessToken, refreshToken, username);
    }


    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            String username = springUser.getUsername();
            return userRepository.findByUsername(username);
        }

        return Optional.empty();
    }

    public ApiResponse verifyAccount(String token) {
        Optional<String> usernameOpt = tokenService.getUsernameByToken(token);
        if (usernameOpt.isPresent()) {
            String username = usernameOpt.get();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ShopException("User Not Found with username - " + username));
            user.setEnabled(true);
            userRepository.save(user);
            tokenService.deleteToken(token);
            return new ApiResponse(200, "User is Enabled");
        } else {
            return new ApiResponse(400, "Invalid Token");
        }
    }
}
