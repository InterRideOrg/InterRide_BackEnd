package com.interride.controller;

import com.interride.dto.request.ForgotPasswordRequest;
import com.interride.dto.request.ResetPasswordRequest;
import com.interride.service.PasswordResetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PasswordResetControllerUnitTest {

    @Mock  private PasswordResetService passwordResetService;

    @InjectMocks
    private PasswordResetController controller;

    private ForgotPasswordRequest buildForgot(String email){
        var dto = new ForgotPasswordRequest();
        dto.setCorreo(email);
        return dto;
    }
    private ResetPasswordRequest buildReset(String pwd){
        var dto = new ResetPasswordRequest();
        dto.setPassword(pwd);
        return dto;
    }

    /* ---------- CP01 ---------- */
    @Test @DisplayName("CP01 – Solicitud de reset exitosa")
    void givenValidEmail_whenForgot_then200() throws Exception {
        var req = buildForgot("juan@mail.com");

        ResponseEntity<?> res = controller.forgot(req);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals("Revisa tu correo", ((Map<?,?>)res.getBody()).get("message"));
        //verify(passwordResetService).createPasswordResetToken(eq("juan@mail.com"));
    }

    /* ---------- CP02 ---------- */
    @Test @DisplayName("CP02 – Email no existente")
    void givenInvalidEmail_whenForgot_thenThrow(){
        var req = buildForgot("no@mail.com");
        //doThrow(new IllegalArgumentException("no user"))
          //      .when(passwordResetService).createPasswordResetToken(eq("no@mail.com"));

        assertThrows(IllegalArgumentException.class, () -> controller.forgot(req));
        //verify(passwordResetService).createPasswordResetToken(eq("no@mail.com"));
    }

    /* ---------- CP03 ---------- */
    @Test @DisplayName("CP03 – Reset exitoso con token válido")
    void givenValidToken_whenReset_then200(){
        var body = buildReset("newPass");
        ResponseEntity<Map<String,String>> res = controller.resetPassword("abc", body);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals("Contraseña restablecida correctamente", res.getBody().get("message"));
        verify(passwordResetService).resetPassword(eq("abc"), eq("newPass"));
    }

    /* ---------- CP04 ---------- */
    @Test @DisplayName("CP04 – Token inválido")
    void givenInvalidToken_whenReset_thenThrow(){
        var body = buildReset("x");
        doThrow(new IllegalArgumentException("bad token"))
                .when(passwordResetService).resetPassword(eq("bad"), eq("x"));

        assertThrows(IllegalArgumentException.class,
                () -> controller.resetPassword("bad", body));
        verify(passwordResetService).resetPassword(eq("bad"), eq("x"));
    }
}
