package com.interride.service.Impl;

import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.response.ConductorRegistroResponse;
import com.interride.model.entity.Conductor;
import com.interride.repository.ConductorRepository;
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
}