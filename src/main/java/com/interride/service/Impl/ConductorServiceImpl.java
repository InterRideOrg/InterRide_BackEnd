package com.interride.service.Impl;

import com.interride.dto.request.ActualizarUsuarioPerfilRequest;
import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.response.ConductorPerfilActualizadoResponse;
import com.interride.dto.response.ConductorPerfilPublicoResponse;
import com.interride.dto.response.ConductorRegistroResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.DuplicateResourceException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.ConductorMapper;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Viaje;
import com.interride.repository.ConductorRepository;
import com.interride.repository.ViajeRepository;
import com.interride.service.ConductorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConductorServiceImpl implements ConductorService {

    private final ConductorRepository conductorRepository;
    private final ViajeRepository viajeRepository;
    private final ConductorMapper conductorMapper;


    @Transactional
    @Override
    public ConductorPerfilPublicoResponse obtenerPerfilConductorAsignado(Integer idViaje) {
        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje con ID " + idViaje + " no encontrado"));

        if (viaje.getConductor() == null) {
            throw new BusinessRuleException("Ningun conductor ha aceptado aún realizar este viaje.");
        }

        return ConductorMapper.toResponse(viaje.getConductor());
    }
}