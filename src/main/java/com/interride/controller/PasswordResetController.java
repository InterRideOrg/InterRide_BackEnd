package com.interride.controller;

import com.interride.dto.request.ForgotPasswordRequest;
import com.interride.dto.request.ResetPasswordRequest;
import com.interride.service.PasswordResetService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@Valid @RequestBody ForgotPasswordRequest dto) throws Exception {
        passwordResetService.createAndSendPasswordResetToken(dto.getCorreo());
        return ResponseEntity.ok(Map.of("message", "Revisa tu correo"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String,String>> resetPassword(
            @RequestParam String token,              // ← viene en la URL
            @Valid @RequestBody ResetPasswordRequest dto) {

        passwordResetService.resetPassword(token, dto.getPassword());
        return ResponseEntity.ok(Map.of("message","Contraseña restablecida correctamente"));
    }
}