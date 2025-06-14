package com.interride.controller;

import com.interride.dto.request.LoginRequest;
import com.interride.dto.response.AuthResponse;
import com.interride.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerLoginUnitTest {

    @Mock private UsuarioService usuarioService;

    @InjectMocks
    private AuthController controller;

    private LoginRequest buildReq(String mail, String pass) {
        var r = new LoginRequest();
        r.setCorreo(mail);
        r.setPassword(pass);
        return r;
    }

    /* ---------- CP01 ---------- */
    @Test
    @DisplayName("CP01 – Credenciales válidas")
    void givenValidCredentials_whenLogin_thenReturnJWT() {
        var req = buildReq("juan@mail.com", "123");
        var expected = mock(AuthResponse.class);

        when(usuarioService.login(eq(req))).thenReturn(expected);

        ResponseEntity<AuthResponse> res = controller.login(req);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(expected, res.getBody());
        verify(usuarioService).login(eq(req));
    }

    /* ---------- CP02 ---------- */
    @Test
    @DisplayName("CP02 – Contraseña incorrecta")
    void givenWrongPassword_whenLogin_thenThrow() {
        var req = buildReq("juan@mail.com", "bad");
        when(usuarioService.login(eq(req)))
                .thenThrow(new BadCredentialsException("bad"));

        assertThrows(BadCredentialsException.class, () -> controller.login(req));
        verify(usuarioService).login(eq(req));
    }

    /* ---------- CP03 ---------- */
    @Test
    @DisplayName("CP03 – Usuario no existe")
    void givenUnknownUser_whenLogin_thenThrow() {
        var req = buildReq("nouser@mail.com", "123");
        when(usuarioService.login(eq(req)))
                .thenThrow(new UsernameNotFoundException("no"));

        assertThrows(UsernameNotFoundException.class, () -> controller.login(req));
        verify(usuarioService).login(eq(req));
    }
}
