package com.interride.controller;

import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.request.GoogleLoginRequest;
import com.interride.dto.response.ConductorRegistroResponse;
import com.interride.dto.request.LoginRequest;
import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.dto.response.AuthResponse;
import com.interride.dto.response.PasajeroRegistroResponse;

import com.interride.dto.response.UsuarioResponse;
import com.interride.mapper.PasajeroMapper;
import com.interride.model.entity.Pasajero;
import com.interride.security.TokenProvider;
import com.interride.security.UserPrincipal;
import com.interride.service.*;
import com.interride.service.Impl.GoogleLoginServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasajeroService pasajeroService;
    private final PasswordResetService passwordResetService;
    private final AuthenticationManager authManager;
    private final TokenProvider tokenProvider;
    private final PasajeroMapper pasajeroMapper;
    private final ModelMapper modelMapper;
    private final ConductorService conductorService;
    private final UsuarioService usuarioService;
    private final GoogleLoginService googleLoginService;


    @PostMapping("/register/pasajero")
    public ResponseEntity<UsuarioResponse> registrarPasajero(
            @Valid @RequestBody PasajeroRegistrationRequest request) {
        UsuarioResponse response = usuarioService.registrarPasajero(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/conductor")
    public ResponseEntity<UsuarioResponse> registrarConductor(@RequestBody @Valid ConductorRegistroRequest request) {
        UsuarioResponse response = usuarioService.registrarConductor(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google-login")
    public ResponseEntity<AuthResponse> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        return ResponseEntity.ok(googleLoginService.loginWithGoogle(request));
    }

/*
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String,String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest dto) {
        passwordResetService.createPasswordResetToken(dto.getCorreo());
        return ResponseEntity.ok(Map.of("message", "Se envió un enlace de recuperación al correo"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String,String>> resetPassword(
            @RequestParam String token,
            @Valid @RequestBody ResetPasswordRequest dto) {

        passwordResetService.resetPassword(token, dto.getPassword());
        return ResponseEntity.ok(Map.of("message","Contraseña actualizada"));
    }*/
}
