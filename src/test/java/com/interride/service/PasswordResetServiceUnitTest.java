package com.interride.service;

import com.interride.integration.notification.email.dto.Mail;
import com.interride.integration.notification.email.service.EmailService;
import com.interride.model.entity.PasswordResetToken;
import com.interride.model.entity.Usuario;
import com.interride.repository.PasswordResetTokenRepository;
import com.interride.repository.UsuarioRepository;
import com.interride.service.Impl.PasswordResetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PasswordResetServiceUnitTest {
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private PasswordResetTokenRepository tokenRepository;
    @Mock private EmailService emailService;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    PasswordResetServiceImpl passwordResetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS
    @Test
    @DisplayName("CP01 - Create and send password reset token - Success")
    void createAndSendPasswordResetToken_Success() throws Exception {
        String email = "example@gmail.com";
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo(email);

        Mail mail = new Mail();

        when(usuarioRepository.findByCorreo(email)).thenReturn(Optional.of(usuario));
        when(tokenRepository.save(any(PasswordResetToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(emailService.createEmail(anyString(), anyString(), anyMap(), anyString())).thenReturn(mail);

        passwordResetService.createAndSendPasswordResetToken(email);

        ArgumentCaptor<PasswordResetToken> tokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        PasswordResetToken savedToken = tokenCaptor.getValue();
        savedToken.setToken(UUID.randomUUID().toString());
        savedToken.setUsuario(usuario);
        savedToken.setExpiration(10);

        assertNotNull(savedToken.getToken());
        assertEquals(usuario, savedToken.getUsuario());

        verify(emailService).sendEmail(eq(mail), eq("email/password-reset-template"));
    }

    @Test
    @DisplayName("CP02 - Find password reset token by token - Success")
    void findByToken_Success() {
        String tokenValue = "test-token";
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenValue);

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        assertEquals(token, passwordResetService.findByToken(tokenValue));
    }

    @Test
    @DisplayName("CP03 - Remove password reset token - Success")
    void removeResetToken_Success() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("test-token");

        passwordResetService.removeResetToken(token);

        verify(tokenRepository).delete(token);
    }

    @Test
    @DisplayName("CP04 - Check if token is valid - Success")
    void isValidToken_Success() {
        String tokenValue = "valid-token";
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenValue);
        token.setExpiration(5); // Token creado hace 5 minutos

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));
        boolean isValid = passwordResetService.isValidToken(tokenValue);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("CP05 - Reset password - Success")
    void resetPassword_Success() {
        String tokenValue = "reset-token";
        String newPassword = "newPassword123";
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(tokenValue);
        resetToken.setExpiration(5); // Token creado hace 5 minutos
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setPassword("oldPassword123");
        resetToken.setUsuario(usuario);

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(resetToken));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        passwordResetService.resetPassword(tokenValue, newPassword);
        verify(usuarioRepository).save(usuario);
        assertEquals("encodedNewPassword", usuario.getPassword());
        verify(tokenRepository).delete(resetToken);
    }

}
