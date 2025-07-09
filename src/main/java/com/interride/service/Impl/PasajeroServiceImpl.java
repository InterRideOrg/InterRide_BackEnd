package com.interride.service.Impl;


import com.interride.model.entity.Pasajero;
import com.interride.repository.PasajeroRepository;
import com.interride.service.PasajeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
