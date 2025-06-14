package com.interride.service.Impl;

import com.interride.model.entity.*;
import com.interride.repository.*;
import com.interride.service.PasswordResetService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;   // ← IMPORT CORRECTO
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {
    private final PasajeroRepository pasajeroRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final JavaMailSender mailSender;
    private final PasswordEncoder encoder;

    @Value("${interride.reset.base-url}")
    private String resetBaseUrl;

    @Override
    public void createPasswordResetToken(String email) {
        //Pasajero user = pasajeroRepo.findByCorreoIgnoreCase(email)
          //      .orElseThrow(() -> new IllegalArgumentException("No existe la cuenta"));

        Pasajero user = new Pasajero();

        // Elimina tokens previos (si quieres)
        tokenRepo.deleteByPasajero(user);

        // Crea y guarda
        String token = UUID.randomUUID().toString();
        var prt = new PasswordResetToken(token, user, Instant.now().plus(Duration.ofHours(1)));
        tokenRepo.save(prt);

        // Envía el correo
        try {
            String link = resetBaseUrl + "?token=" + token;
            var msg = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setTo(
                    "xd"
                    //user.getCorreo()
            );
            helper.setSubject("Restablece tu contraseña");
            helper.setText("""
                Hola %s,

                Has solicitado restablecer tu contraseña.
                Haz clic en: %s

                Si no fuiste tú, ignora este correo.
                """.formatted(user.getNombre(), link));

            mailSender.send(msg);
        } catch (MailException | MessagingException e) {
            throw new IllegalStateException("No se pudo enviar el correo de recuperación", e);
        }
    }

    @Override
    public void resetPassword(String token, String rawPassword) {
        var prt = tokenRepo.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (prt.isExpired()) {
            throw new IllegalStateException("Token expirado");
        }

        var user = prt.getPasajero();
        //user.setPassword(encoder.encode(rawPassword));
        pasajeroRepo.save(user);

        tokenRepo.delete(prt);
    }
}
