package com.interride.service.Impl;

import com.interride.dto.request.ReclamoRequest;
import com.interride.dto.response.ReclamoResponse;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Reclamo;
import com.interride.repository.ConductorRepository;
import com.interride.repository.PasajeroRepository;
import com.interride.repository.ReclamoRepository;
import com.interride.service.ReclamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReclamoServiceImpl implements ReclamoService {

    private final ReclamoRepository reclamoRepository;
    private final PasajeroRepository pasajeroRepository;
    private final ConductorRepository conductorRepository;

    @Override
    public ReclamoResponse crearReclamo(ReclamoRequest request) {
        if (request.mensaje() == null || request.mensaje().trim().isEmpty()) {
            throw new RuntimeException("El mensaje no puede estar vacío.");
        }

        Reclamo reclamo = new Reclamo();
        reclamo.setMensaje(request.mensaje());
        reclamo.setFechaHoraEnvio(LocalDateTime.now());

        if (request.idConductor() != null) {
            Conductor conductor = conductorRepository.findById(request.idConductor())
                    .orElseThrow(() -> new RuntimeException("Conductor no encontrado."));
            reclamo.setConductor(conductor);
        } else if (request.idPasajero() != null) {
            Pasajero pasajero = pasajeroRepository.findById(request.idPasajero())
                    .orElseThrow(() -> new RuntimeException("Pasajero no encontrado."));
            reclamo.setPasajero(pasajero);
        } else {
            throw new RuntimeException("Debe especificar algun ID.");
        }

        Reclamo nuevoReclamo = reclamoRepository.save(reclamo);

        return new ReclamoResponse(
                nuevoReclamo.getId(),
                "Reclamo enviado. Nos comunicaremos en 24h." ,
                nuevoReclamo.getFechaHoraEnvio()
        );
    }

}
