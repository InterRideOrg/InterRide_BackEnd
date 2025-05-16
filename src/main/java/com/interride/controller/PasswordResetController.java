package com.interride.controller;

import com.interride.dto.*;
import com.interride.service.PasswordResetService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@Valid @RequestBody ForgotPasswordDTO dto) throws MessagingException {
        passwordResetService.createPasswordResetToken(dto.getCorreo());
        return ResponseEntity.ok(Map.of("message", "Revisa tu correo"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String,String>> resetPassword(
            @RequestParam String token,              // ← viene en la URL
            @Valid @RequestBody ResetPasswordDTO dto) {

        passwordResetService.resetPassword(token, dto.getPassword());
        return ResponseEntity.ok(Map.of("message","Contraseña restablecida correctamente"));
    }
}