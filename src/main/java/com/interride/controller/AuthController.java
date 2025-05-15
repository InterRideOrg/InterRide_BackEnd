package com.interride.controller;

import com.interride.dto.*;
import com.interride.mapper.PasajeroMapper;
import com.interride.model.entity.Pasajero;
import com.interride.security.TokenProvider;
import com.interride.security.UserPrincipal;
import com.interride.service.PasajeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    /* ---------- registro ---------- */
    @PostMapping("/register")
    public ResponseEntity<PasajeroProfileDTO> register(
            @Valid @RequestBody PasajeroRegistrationDTO dto) {

        PasajeroProfileDTO profile = pasajeroService.register(dto);
        return ResponseEntity.status(201).body(profile);
    }

    /* ---------- login ---------- */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getCorreo(), loginDTO.getPassword())
        );

        // ①  Obtenemos el Pasajero desde la autenticación
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Pasajero pasajero = pasajeroService.getById(principal.getId());

        // ②  Mapeamos a DTO
        PasajeroProfileDTO profileDTO = modelMapper.map(pasajero, PasajeroProfileDTO.class);

        // ③  Construimos la respuesta
        AuthResponseDTO response = new AuthResponseDTO(
                tokenProvider.createAccessToken(auth),
                tokenProvider.getExpiration(),
                "Bearer",
                profileDTO              //  ← ¡Aquí ya no será null!
        );
        return ResponseEntity.ok(response);
    }
}
