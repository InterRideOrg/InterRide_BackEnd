package com.interride.service.Impl;

import com.interride.exception.ResourceNotFoundException;
import com.interride.integration.notification.email.dto.Mail;
import com.interride.integration.notification.email.service.EmailService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    //@Value("${spring.mail.username}")
    private String mailFrom = "interrideorg@gmail.com";

    @Transactional
    @Override
    public void createAndSendPasswordResetToken(String email) throws Exception{
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));


        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setUsuario(usuario);
        passwordResetToken.setExpiration(10);
        tokenRepository.save(passwordResetToken);

        Map<String, Object> model = new HashMap <>();
        String resetUrl = "http://localhost:5173/forgot/" + passwordResetToken.getToken();
        model.put("user", usuario.getCorreo());
        model.put("resetUrl", resetUrl);

        Mail mail = emailService.createEmail(
                usuario.getCorreo(),
                "Restablecer contraseña",
                model,
                mailFrom
        );

        emailService.sendEmail(mail, "email/password-reset-template");
    }


    @Override
    public PasswordResetToken findByToken(String token){
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token de restablecimiento de contraseña no encontrado"));
    }

    @Override
    public void removeResetToken(PasswordResetToken token){
        tokenRepository.delete(token);
    }

    @Override
    public boolean isValidToken(String token){
        return tokenRepository.findByToken(token)
                .filter(t->!t.isExpired())
                .isPresent();
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .filter(t->!t.isExpired())
                .orElseThrow(() -> new ResourceNotFoundException("Token invalido o expirado"));

        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
        tokenRepository.delete(resetToken);
    }
}
