package com.interride.service.Impl;


import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.PasajeroMapper;
import com.interride.model.entity.Pasajero;
import com.interride.repository.PasajeroRepository;
import com.interride.service.PasajeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasajeroServiceImpl implements PasajeroService {

    private final PasajeroRepository pasajeroRepository;
    private final PasajeroMapper pasajeroMapper;

    @Override
    @Transactional(readOnly = true)
    public Pasajero getById(Integer id) {
        return pasajeroRepository.findById(id).orElseThrow();
    }


    @Transactional
    @Override
    public PasajeroPerfilPublicoResponse obtenerPerfilPasajero(Integer idPasajero) {
        Pasajero pasajero = pasajeroRepository.findById(idPasajero)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero con ID " + idPasajero + " no encontrado."));

        return pasajeroMapper.toPublicProfileDTO(pasajero);
    }



}
