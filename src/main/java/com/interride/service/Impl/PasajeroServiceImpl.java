package com.interride.service.Impl;


import com.interride.dto.request.ActualizarPasajeroPerfilRequest;
import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.dto.response.PasajeroProfileResponse;
import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.exception.ResourceNotFoundException;
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
    private final PasajeroMapper pasajeroMapper;

    @Transactional
    @Override
    public PasajeroPerfilPublicoResponse obtenerPerfilPasajero(Integer idPasajero) {
        Pasajero pasajero = pasajeroRepository.findById(idPasajero)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero con ID " + idPasajero + " no encontrado."));

        return pasajeroMapper.toPublicProfileDTO(pasajero);
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

    @Override
    @Transactional
    public PasajeroPerfilPublicoResponse actualizarPerfilPasajero(Integer id, ActualizarPasajeroPerfilRequest perfilActualizado) {
        Pasajero pasajero = pasajeroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pasajero con ID " + id + " no encontrado."));

        // Actualizar los campos del pasajero
        if (perfilActualizado.getNombres() != null && !perfilActualizado.getNombres().isEmpty()) {
            pasajero.setNombre(perfilActualizado.getNombres());
        }
        if (perfilActualizado.getApellidos() != null && !perfilActualizado.getApellidos().isEmpty()) {
            pasajero.setApellidos(perfilActualizado.getApellidos());
        }
        if (perfilActualizado.getTelefono() != null && !perfilActualizado.getTelefono().isEmpty()) {
            // Verificar si el teléfono ya está registrado por otro pasajero
            if (pasajeroRepository.existsByTelefono(perfilActualizado.getTelefono())) {
                throw new RuntimeException("El teléfono ya está registrado por otro pasajero.");
            }
            pasajero.setTelefono(perfilActualizado.getTelefono());
        }
        if (perfilActualizado.getCorreo() != null && !perfilActualizado.getCorreo().isEmpty()) {
            // Verificar si el correo ya está registrado por otro pasajero
            if (pasajeroRepository.existsByCorreo(perfilActualizado.getCorreo())) {
                throw new RuntimeException("El correo ya está registrado por otro pasajero.");
            }
            pasajero.setCorreo(perfilActualizado.getCorreo());
        }

        // Guardar los cambios
        Pasajero updatedPasajero = pasajeroRepository.save(pasajero);

        return new PasajeroPerfilPublicoResponse(
                updatedPasajero.getNombre(),
                updatedPasajero.getApellidos(),
                updatedPasajero.getCorreo(),
                updatedPasajero.getTelefono(),
                updatedPasajero.getUsername()
        );
    }
}
