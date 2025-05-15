package com.interride.controller;

import com.interride.dto.*;
import com.interride.security.TokenProvider;
import com.interride.service.PasajeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasajeroService pasajeroService;
    private final AuthenticationManager authManager;
    private final TokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<PasajeroProfileDTO> register(
            @Valid @RequestBody PasajeroRegistrationDTO dto) {

        PasajeroProfileDTO resp = pasajeroService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginDTO dto) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        String token = tokenProvider.createToken(auth);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
