package com.interride.service;

import com.interride.dto.ConductorAsignadoResponse;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.PasajeroViaje;
import com.interride.model.entity.Vehiculo;
import com.interride.repository.PasajeroViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConductorAsignadoService {

    @Autowired
    private PasajeroViajeRepository pasajeroViajeRepository;

    public ConductorAsignadoResponse obtenerConductorAsignado(Integer pasajeroViajeId, Integer pasajeroId) {
        PasajeroViaje pasajeroViaje = pasajeroViajeRepository.findByIdAndPasajeroId(pasajeroViajeId, pasajeroId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado o no pertenece al pasajero"));

        Conductor conductor = pasajeroViaje.getViaje().getConductor();
        Vehiculo vehiculo = conductor.getVehiculo();

        ConductorAsignadoResponse dto = new ConductorAsignadoResponse();
        dto.setNombreCompletoConductor(conductor.getNombre() + " " + conductor.getApellidos());
        dto.setPlacaVehiculo(vehiculo.getPlaca());

        return dto;
    }
}
