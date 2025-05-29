package com.interride.mapper;

import com.interride.dto.response.PasajeroViajesResponse;
import com.interride.dto.response.ViajeAceptadoResponse;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.PasajeroViaje;
import com.interride.model.entity.Ubicacion;
import com.interride.model.entity.Viaje;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ViajeMapper {
    /*
    Integer idViaje,
        EstadoViaje estadoViaje,
        String provinciaOrigen,
        String provinciaDestino,
        String direccionOrigen,
        LocalDateTime fechaHoraPartida,
        Integer asientosDisponibles
     */
    public ViajeAceptadoResponse toViajeAceptadoResponse(Viaje viaje, Conductor conductor, Ubicacion origen, Ubicacion destino) {
        return new ViajeAceptadoResponse(
                viaje.getId(),
                conductor.getId(),
                viaje.getEstado(),
                origen.getProvincia(),
                destino.getProvincia(),
                origen.getDireccion(),
                viaje.getFechaHoraPartida(),
                viaje.getAsientosDisponibles()
        );
    }
}
