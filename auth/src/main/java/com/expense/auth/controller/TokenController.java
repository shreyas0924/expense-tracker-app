package com.expense.auth.controller;

import com.expense.auth.entities.RefreshToken;
import com.expense.auth.request.AuthRequestDto;
import com.expense.auth.request.RefreshTokenRequestDto;
import com.expense.auth.response.JwtResponseDto;
import com.expense.auth.service.JwtService;
import com.expense.auth.service.RefreshTokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @PostMapping("auth/v1/login")
    public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDto authRequestDTO) {
        logger.info("Login request received for user: {}", authRequestDTO.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(),
                            authRequestDTO.getPassword()));
            if (authentication.isAuthenticated()) {
                logger.info("User authenticated successfully: {}", authRequestDTO.getUsername());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
                logger.info("Refresh token created: {}", refreshToken.getToken());
                String accessToken = jwtService.GenerateToken(authRequestDTO.getUsername());
                logger.info("Access token generated: {}", accessToken);
                return new ResponseEntity<>(JwtResponseDto.builder()
                        .accessToken(accessToken)
                        .token(refreshToken.getToken())
                        .build(), HttpStatus.OK);
            } else {
                logger.error("Authentication failed for user: {}", authRequestDTO.getUsername());
                return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Exception during authentication for user: {}", authRequestDTO.getUsername(), e);
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("auth/v1/refreshToken")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDTO) {
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return JwtResponseDto.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
    }
}
