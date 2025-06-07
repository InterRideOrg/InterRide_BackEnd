package com.interride.service.Impl;

import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.response.ConductorPerfilPublicoResponse;
import com.interride.dto.response.ConductorRegistroResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.ConductorMapper;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Viaje;
import com.interride.repository.ConductorRepository;
import com.interride.repository.ViajeRepository;
import com.interride.service.ConductorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConductorServiceImpl implements ConductorService {

    private final ConductorRepository conductorRepository;
    private final PasswordEncoder passwordEncoder;
    private final ViajeRepository viajeRepository;

    @Override
    public ConductorRegistroResponse registrarConductor(ConductorRegistroRequest request) {

        if (conductorRepository.existsByCorreo(request.correo())) {
            throw new RuntimeException("Correo ya registrado. Ingrese otro.");
        }

        if (conductorRepository.existsByTelefono(request.telefono())) {
            throw new RuntimeException("Teléfono ya registrado. Ingrese otro.");
        }

        if (conductorRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username ya registrado. Ingrese otro.");
        }

        Conductor conductor = new Conductor();
        conductor.setNombre(request.nombre());
        conductor.setApellidos(request.apellidos());
        conductor.setCorreo(request.correo());
        conductor.setTelefono(request.telefono());
        conductor.setUsername(request.username());
        conductor.setPassword(passwordEncoder.encode(request.password()));
        conductor.setFechaHoraRegistro(LocalDateTime.now());

        conductorRepository.save(conductor);


        return new ConductorRegistroResponse("Registro exitoso. Se le ha enviado un correo de confirmación.");
    }


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