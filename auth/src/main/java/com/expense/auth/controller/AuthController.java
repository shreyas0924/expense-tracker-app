package com.expense.auth.controller;

import com.expense.auth.entities.RefreshToken;
import com.expense.auth.model.UserInfoDto;
import com.expense.auth.response.JwtResponseDto;
import com.expense.auth.service.JwtService;
import com.expense.auth.service.RefreshTokenService;
import com.expense.auth.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @SuppressWarnings("rawtypes")
    @PostMapping("auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto) {
        try {
            Boolean isSignUped = userDetailsService.signupUser(userInfoDto);
            if (Boolean.FALSE.equals(isSignUped)) {
                return new ResponseEntity<>("Already Exist", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());
            return new ResponseEntity<>(
                    JwtResponseDto.builder().accessToken(jwtToken).token(refreshToken.getToken()).build(),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("auth/v1/ping")
    public ResponseEntity<String> ping() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            
            String userId = userDetailsService.getUserByUsername(authentication.getName());
            if (Objects.nonNull(userId)) {
                return ResponseEntity.ok(userId);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @GetMapping("health")
    public ResponseEntity<Boolean> checkHealth() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
