package com.hungum.shop.controller;

import com.hungum.shop.dto.AuthenticationResponse;
import com.hungum.shop.dto.LoginRequestDto;
import com.hungum.shop.dto.RegisterRequestDto;
import com.hungum.shop.exceptions.ApiResponse;
import com.hungum.shop.service.AuthService;
import com.hungum.shop.service.JwtTokenProvider;
import com.hungum.shop.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth/")
@Slf4j
public class AuthController {

    private final AuthService authService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TokenService tokenService;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(authService.authenticate(loginRequestDto), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        if (authService.existsByUserName(registerRequestDto)) {
            return new ResponseEntity<>(new ApiResponse(400, "Username already exists"), HttpStatus.BAD_REQUEST);
        }

        authService.createUser(registerRequestDto);
        return new ResponseEntity<>(new ApiResponse(200, "User Registration Completed Successfully!!"), HttpStatus.OK);
    }
    @PostMapping("refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(refreshToken);
            Optional<String> storedRefreshToken = tokenService.getRefreshToken(username);

            if (storedRefreshToken.isPresent() && storedRefreshToken.get().equals(refreshToken)) {
                String newAccessToken = jwtTokenProvider.generateRefreshToken(username, Duration.ofMinutes(15).toMillis());
                return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, refreshToken, username));
            } else {
                return ResponseEntity.status(401).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }


    @GetMapping("accountVerification/{token}")
    public ApiResponse verifyAccount(@PathVariable String token) {
        return authService.verifyAccount(token);
    }
}
