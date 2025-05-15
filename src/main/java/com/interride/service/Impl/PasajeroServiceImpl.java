package com.interride.service.impl;

import com.interride.dto.*;
import com.interride.mapper.PasajeroMapper;
import com.interride.model.entity.Pasajero;
import com.interride.repository.PasajeroRepository;
import com.interride.service.EmailService;
import com.interride.service.PasajeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasajeroServiceImpl implements PasajeroService {

    private final PasajeroRepository repo;
    private final PasajeroMapper mapper;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    @Transactional
    @Override
    public PasajeroProfileDTO register(PasajeroRegistrationDTO dto) {

        if (repo.existsByCorreo(dto.getCorreo()))
            throw new RuntimeException("El correo ya está registrado");
        if (repo.existsByTelefono(dto.getTelefono()))
            throw new RuntimeException("El teléfono ya está registrado");

        Pasajero entity = mapper.toEntity(dto);
        entity.setPassword(encoder.encode(dto.getPassword()));
        entity.setFechaHoraRegistro(LocalDateTime.now());

        Pasajero saved = repo.save(entity);

        emailService.sendRegistrationConfirmation(
                saved.getCorreo(),
                "Bienvenido a InterRide",
                String.format("Hola %s, gracias por registrarte.", saved.getNombre())
        );

        return mapper.toProfileDTO(saved);
    }
}
