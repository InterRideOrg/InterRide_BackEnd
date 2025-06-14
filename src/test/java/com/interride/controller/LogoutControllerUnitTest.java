package com.interride.controller;

import com.interride.security.TokenProvider;
import com.interride.service.TokenBlacklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutControllerUnitTest {

    @Mock private TokenProvider tokenProvider;
    @Mock private TokenBlacklistService blacklist;

    @InjectMocks
    private LogoutController controller;

    private String bearer;

    @BeforeEach
    void setUp() {
        bearer = "Bearer abc.jwt.token";
    }

    @Test
    @DisplayName("CP01 – Token válido, logout exitoso")
    void givenValidToken_whenLogout_thenBlacklistAnd200() {
        when(tokenProvider.getExpirationDate("abc.jwt.token"))
                .thenReturn(Instant.now().plusSeconds(600));

        ResponseEntity<Map<String,String>> res = controller.logout(bearer);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals("Sesión cerrada correctamente", res.getBody().get("message"));
        verify(blacklist).add(eq("abc.jwt.token"), any(Instant.class));
    }

    @Test
    @DisplayName("CP02 – Token mal formado")
    void givenMalformedToken_whenLogout_thenThrow() {
        String badBearer = "abc.jwt";
        when(tokenProvider.getExpirationDate("abc.jwt"))
                .thenThrow(new RuntimeException("malformed"));

        assertThrows(RuntimeException.class, () -> controller.logout(badBearer));
        verifyNoInteractions(blacklist);
    }
}
