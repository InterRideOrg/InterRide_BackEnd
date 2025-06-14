package com.interride.service;

import com.interride.model.entity.Pasajero;
import com.interride.model.entity.PasswordResetToken;
import com.interride.repository.PasajeroRepository;
import com.interride.repository.PasswordResetTokenRepository;
import com.interride.service.Impl.PasswordResetServiceImpl;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceUnitTest {

    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private PasswordResetTokenRepository tokenRepository;
    @Mock private JavaMailSender mailSender;
    @Mock private PasswordEncoder encoder;

    @Mock private MimeMessage mimeMsg;

    @InjectMocks
    private PasswordResetServiceImpl service;

    private Pasajero pasajero;

    @BeforeEach
    void setUp() {
        pasajero = new Pasajero();
        pasajero.setId(1);
        pasajero.setCorreo("juan@mail.com");
        pasajero.setNombre("Juan");
        pasajero.setPassword("old");
    }

    @Test
    @DisplayName("CP01 – Solicitud con correo válido")
    void givenValidEmail_whenRequestReset_thenSendMail() {
        when(pasajeroRepository.findByCorreoIgnoreCase("juan@mail.com"))
                .thenReturn(Optional.of(pasajero));
        when(mailSender.createMimeMessage()).thenReturn(mimeMsg);

        service.createPasswordResetToken("juan@mail.com");

        verify(tokenRepository).save(any(PasswordResetToken.class));
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("CP02 – Correo no existe")
    void givenInvalidEmail_whenRequestReset_thenThrow() {
        when(pasajeroRepository.findByCorreoIgnoreCase("juan@mail.com"))
                .thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.createPasswordResetToken("juan@mail.com"));
    }

    @Test
    @DisplayName("CP03 – Token válido, contraseña cambiada")
    void givenValidToken_whenResetPassword_thenUpdatePassword() {
        PasswordResetToken token = new PasswordResetToken("abc", pasajero,
                Instant.now().plusSeconds(900));
        when(tokenRepository.findByToken("abc")).thenReturn(Optional.of(token));
        when(encoder.encode("newPass")).thenReturn("enc");

        service.resetPassword("abc", "newPass");

        assertEquals("enc", pasajero.getPassword());
        verify(pasajeroRepository).save(pasajero);
        verify(tokenRepository).delete(token);
    }

    @Test
    @DisplayName("CP04 – Token inválido")
    void givenInvalidToken_whenResetPassword_thenThrow() {
        when(tokenRepository.findByToken("bad")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.resetPassword("bad", "x"));
    }
}
