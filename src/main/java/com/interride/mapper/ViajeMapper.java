package com.interride.mapper;

import com.interride.dto.request.ViajeSolicitadoRequest;
import com.interride.dto.response.ViajeAceptadoResponse;
import com.interride.dto.response.ViajeSolicitadoResponse;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.PasajeroViaje;
import com.interride.model.entity.Ubicacion;
import com.interride.model.entity.Viaje;
import com.interride.model.enums.EstadoViaje;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ViajeMapper {
    public Viaje toEntity(ViajeSolicitadoRequest request) {
        if (request == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return Viaje.builder()
                .fechaHoraCreacion(LocalDateTime.now())
                .fechaHoraPartida(LocalDateTime.parse(request.fechaHoraPartida(), formatter))
                .asientosDisponibles(0)
                .asientosOcupados(request.asientosReservados())
                .estado(EstadoViaje.SOLICITADO)
                .build();
    }



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

    public ViajeSolicitadoResponse toViajeSolicitadoResponse(Viaje viaje, PasajeroViaje boleto, Ubicacion origen, Ubicacion destino) {
        return new ViajeSolicitadoResponse(
                viaje.getId(),
                viaje.getFechaHoraPartida().toString(),
                boleto.getFechaHoraLLegada().toString(),
                origen.getProvincia(),
                destino.getProvincia(),
                origen.getDireccion(),
                destino.getDireccion(),
                boleto.getCosto(),
                boleto.getAsientosOcupados(),
                viaje.getEstado().toString()
        );
    }
}
