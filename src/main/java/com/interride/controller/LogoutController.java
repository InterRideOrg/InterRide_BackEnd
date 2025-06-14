package com.interride.controller;

import com.interride.security.TokenProvider;
import com.interride.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LogoutController {

    private final TokenProvider tokenProvider;          // para parsear el JWT
    private final TokenBlacklistService blacklist;      // servicio opcional

    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(
            @RequestHeader("Authorization") String bearer) {

        String token = bearer.replaceFirst("(?i)Bearer\\s+", "");
        //blacklist.add(token, tokenProvider.getExpirationDate(token));

        return ResponseEntity.ok(
                Map.of("message", "Sesión cerrada correctamente"));
    }
}
