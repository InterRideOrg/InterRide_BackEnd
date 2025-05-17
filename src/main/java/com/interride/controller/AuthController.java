package com.interride.controller;

import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.response.ConductorRegistroResponse;
import com.interride.dto.request.LoginRequest;
import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.dto.response.AuthResponse;
import com.interride.dto.response.PasajeroProfileResponse;

import com.interride.mapper.PasajeroMapper;
import com.interride.model.entity.Pasajero;
import com.interride.security.TokenProvider;
import com.interride.security.UserPrincipal;
import com.interride.service.ConductorService;
import com.interride.service.PasajeroService;
import com.interride.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    /* ---------- registro ---------- */
    @PostMapping("/register")
    public ResponseEntity<PasajeroProfileResponse> register(
            @Valid @RequestBody PasajeroRegistrationRequest dto) {

        PasajeroProfileResponse profile = pasajeroService.register(dto);
        return ResponseEntity.status(201).body(profile);
    }

    @PostMapping("/registerConductor")
    public ResponseEntity<ConductorRegistroResponse> registrar(@RequestBody @Valid ConductorRegistroRequest request) {
        ConductorRegistroResponse response = conductorService.registrarConductor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /* ---------- login ---------- */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getCorreo(), loginRequest.getPassword())
        );

        // ①  Obtenemos el Pasajero desde la autenticación
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Pasajero pasajero = pasajeroService.getById(principal.getId());

        // ②  Mapeamos a DTO
        PasajeroProfileResponse profileDTO = modelMapper.map(pasajero, PasajeroProfileResponse.class);

        // ③  Construimos la respuesta
        AuthResponse response = new AuthResponse(
                tokenProvider.createAccessToken(auth),
                tokenProvider.getExpiration(),
                "Bearer",
                profileDTO              //  ← ¡Aquí ya no será null!
        );
        return ResponseEntity.ok(response);
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
