package com.vehicletrading.controller;

import com.vehicletrading.dto.AuthResponse;
import com.vehicletrading.dto.LoginRequest;
import com.vehicletrading.dto.RegisterRequest;
import com.vehicletrading.dto.UserResponse;
import com.vehicletrading.security.JwtUtil;
import com.vehicletrading.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login, Register, OAuth2")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "Register a new user",
        responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class))))
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and receive JWT token",
        responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AuthResponse.class))))
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(new AuthResponse(token, userDetails.getUsername(), role));
    }

    @GetMapping("/oauth2/success")
    @Operation(summary = "OAuth2 login success - returns JWT token")
    public ResponseEntity<AuthResponse> oauth2Success(@RequestParam String token) {
        String username = jwtUtil.extractUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(new AuthResponse(token, username, role));
    }
}
