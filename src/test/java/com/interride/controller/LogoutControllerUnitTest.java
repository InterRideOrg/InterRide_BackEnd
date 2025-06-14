package com.interride.controller;

import com.interride.security.TokenProvider;
import com.interride.service.TokenBlacklistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class LogoutControllerUnitTest {

    @Mock  private TokenProvider tokenProvider;
    @Mock  private TokenBlacklistService blacklist;

    @InjectMocks
    private LogoutController controller;

    /* ---------- CP01 ---------- */
    @Test
    @DisplayName("CP01 – Logout exitoso con header estándar")
    void givenBearerHeader_whenLogout_then200() {
        String header = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

        ResponseEntity<Map<String,String>> res = controller.logout(header);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsEntry("message", "Sesión cerrada correctamente");
        // La versión actual del controller no llama a blacklist
        verifyNoInteractions(blacklist, tokenProvider);
    }

    /* ---------- CP02 ---------- */
    @Test
    @DisplayName("CP02 – Logout con prefijo en minúsculas (case‑insensitive)")
    void givenLowerCaseBearer_whenLogout_then200() {
        String header = "bearer ABC.DEF.GHI";

        ResponseEntity<Map<String,String>> res = controller.logout(header);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsEntry("message", "Sesión cerrada correctamente");
        verifyNoInteractions(blacklist, tokenProvider);
    }
}
