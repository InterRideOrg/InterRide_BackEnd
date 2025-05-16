package com.interride.service.Impl;

import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.model.entity.Pasajero;
import com.interride.repository.PasajeroRepository;
import com.interride.service.PasajeroService;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PasajeroServiceImpl implements PasajeroService {

    private final PasajeroRepository pasajeroRepository;

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
}
