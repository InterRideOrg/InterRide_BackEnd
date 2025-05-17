package com.interride.service.Impl;


import com.interride.dto.*;
import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.dto.response.PasajeroProfileResponse;
import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.mapper.PasajeroMapper;
import com.interride.model.entity.Pasajero;
import jakarta.persistence.EntityNotFoundException;      
import com.interride.repository.PasajeroRepository;
import com.interride.service.EmailService;
import com.interride.service.PasajeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Alta de pasajeros (HU-01) - ahora deja la contraseña
 * cifrada antes de persistir y de mapearla de vuelta a DTO.
 */
@Service
@RequiredArgsConstructor
public class PasajeroServiceImpl implements PasajeroService {

    private final PasajeroRepository pasajeroRepository;
    private final PasajeroMapper mapper;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    @Transactional
    @Override
    public PasajeroPerfilPublicoResponse obtenerPerfilPasajero(Integer idPasajero) {
        Pasajero pasajero = pasajeroRepository.findById(idPasajero)
                .orElseThrow(() -> new EntityNotFoundException("Pasajero con ID " + idPasajero + " no encontrado."));

        return new PasajeroPerfilPublicoResponse(
                pasajero.getNombre(),
                pasajero.getApellidos(),
                pasajero.getCorreo(),
                pasajero.getTelefono(),
                pasajero.getUsername()
        );
    }

    @Transactional
    @Override
    public PasajeroProfileResponse register(PasajeroRegistrationRequest dto) {

        if (pasajeroRepository.existsByCorreo(dto.getCorreo()))
            throw new RuntimeException("El correo ya está registrado");
        if (pasajeroRepository.existsByTelefono(dto.getTelefono()))
            throw new RuntimeException("El teléfono ya está registrado");
        // Map DTO -> Entity
        Pasajero entity = mapper.toEntity(dto);
        entity.setPassword(encoder.encode(dto.getPassword()));
        entity.setFechaHoraRegistro(LocalDateTime.now());
        Pasajero saved = pasajeroRepository.save(entity);
        // Correo de bienvenida
        emailService.sendRegistrationConfirmation(
                saved.getCorreo(),
                "Bienvenido a InterRide",
                "Hola " + saved.getNombre()
                        + ", ¡gracias por registrarte en InterRide!"
        );
        // Devolvemos DTO limpio (sin password)
        return mapper.toProfileDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Pasajero getById(Integer id) {
        return pasajeroRepository.findById(id).orElseThrow();
    }
}
