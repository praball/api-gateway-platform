package com.apigatewayplatform.gatewayservice.controller;

import com.apigatewayplatform.gatewayservice.dto.LoginRequest;
import com.apigatewayplatform.gatewayservice.dto.LoginResponse;
import com.apigatewayplatform.gatewayservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();

        if (!username.equals("admin") && !username.equals("user")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String role = username.equals("admin") ? "ADMIN" : "USER";

        String token = jwtUtil.generateToken(username, role);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
