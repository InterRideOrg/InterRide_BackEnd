package com.interride.controller;

import com.interride.dto.*;
import com.interride.mapper.PasajeroMapper;
import com.interride.security.TokenProvider;
import com.interride.security.UserPrincipal;
import com.interride.service.PasajeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasajeroService pasajeroService;
    private final AuthenticationManager authManager;
    private final TokenProvider tokenProvider;
    private final PasajeroMapper pasajeroMapper;

    /* ---------- registro ---------- */
    @PostMapping("/register")
    public ResponseEntity<PasajeroProfileDTO> register(
            @Valid @RequestBody PasajeroRegistrationDTO dto) {

        PasajeroProfileDTO profile = pasajeroService.register(dto);
        return ResponseEntity.status(201).body(profile);
    }

    /* ---------- login ---------- */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginDTO dto) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getCorreo(), dto.getPassword())
        );

        String token = tokenProvider.createAccessToken(auth);
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token(token)
                .expiresIn(tokenProvider.getExpiration())   //  ←  usa el nombre correcto
                .profile(null)                              //  mapper.toProfileDTO(...) si lo deseas
                .build();

        return ResponseEntity.ok(response);
    }
}
