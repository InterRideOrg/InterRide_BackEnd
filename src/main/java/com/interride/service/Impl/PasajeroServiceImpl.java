package com.interride.service.Impl;


import com.interride.dto.request.ActualizarPasajeroPerfilRequest;
import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.dto.response.PasajeroRegistroResponse;
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

    @Override
    @Transactional(readOnly = true)
    public Pasajero getById(Integer id) {
        return pasajeroRepository.findById(id).orElseThrow();
    }

}
