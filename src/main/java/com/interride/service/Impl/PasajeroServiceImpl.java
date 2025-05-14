package com.interride.service.Impl;

import com.interride.model.entity.Pasajero;
import com.interride.repository.PasajeroRepository;
import com.interride.service.PasajeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PasajeroServiceImpl implements PasajeroService {
    private final PasajeroRepository pasajeroRepository;

    @Transactional
    @Override
    public Pasajero registerPasajero(Pasajero pasajero){
        if(pasajeroRepository.existsByCorreo(pasajero.getCorreo())){
            throw new RuntimeException("El email ya está registrado");
        }
        if(pasajeroRepository.existsByTelefono(pasajero.getTelefono())){
            throw new RuntimeException("El teléfono ya está registrado");
        }

        pasajero.setFechaHoraRegistro(LocalDateTime.now());

        return pasajeroRepository.save(pasajero);
    }
}
