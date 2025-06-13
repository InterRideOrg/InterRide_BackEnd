package com.interride.service.Impl;

import com.interride.dto.request.ActualizarConductorPerfilRequest;
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

    @Override
    public ConductorRegistroResponse registrarConductor(ConductorRegistroRequest request) {

        if (conductorRepository.existsByCorreo(request.correo())) {
            throw new DuplicateResourceException("Correo ya registrado. Ingrese otro.");
        }

        if (conductorRepository.existsByTelefono(request.telefono())) {
            throw new DuplicateResourceException("Teléfono ya registrado. Ingrese otro.");
        }

        if (conductorRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username ya registrado. Ingrese otro.");
        }

        Conductor conductor = conductorMapper.toEntity(request);
        conductorRepository.save(conductor);

        return new ConductorRegistroResponse("Registro exitoso. Se le ha enviado un correo de confirmación.");
    }

    @Override
    @Transactional
    public ConductorPerfilActualizadoResponse actualizarPerfilConductor(Integer id, ActualizarConductorPerfilRequest perfilActualizado) {
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conductor con ID " + id + " no encontrado."));

        // Actualizar los campos del conductor
        if (perfilActualizado.getNombres() != null && !perfilActualizado.getNombres().isEmpty()) {
            conductor.setNombre(perfilActualizado.getNombres());
        }
        if (perfilActualizado.getApellidos() != null && !perfilActualizado.getApellidos().isEmpty()) {
            conductor.setApellidos(perfilActualizado.getApellidos());
        }
        if (perfilActualizado.getTelefono() != null && !perfilActualizado.getTelefono().isEmpty()) {
            if (conductorRepository.existsByTelefono(perfilActualizado.getTelefono())) {
                throw new RuntimeException("Teléfono ya registrado. Ingrese otro.");
            }
            conductor.setTelefono(perfilActualizado.getTelefono());
        }
        if (perfilActualizado.getCorreo() != null && !perfilActualizado.getCorreo().isEmpty()) {
            if (conductorRepository.existsByCorreo(perfilActualizado.getCorreo())) {
                throw new RuntimeException("Correo ya registrado. Ingrese otro.");
            }
            conductor.setCorreo(perfilActualizado.getCorreo());
        }

        //save
        Conductor updatedConductor = conductorRepository.save(conductor);

        return new ConductorPerfilActualizadoResponse(
                updatedConductor.getNombre(),
                updatedConductor.getApellidos(),
                updatedConductor.getCorreo(),
                updatedConductor.getTelefono(),
                updatedConductor.getUsername()
        );
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