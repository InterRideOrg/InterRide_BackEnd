package com.interride.mapper;

import com.interride.dto.request.CreatePagoRequest;
import com.interride.dto.request.UpdatePagoRequest;
import com.interride.dto.response.PagoResponse;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pago;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Viaje;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {
    public Pago toEntity(CreatePagoRequest request) {
        if(request == null) {
            return null;
        }
        return Pago.builder()
                .estado(request.estado())
                .monto(request.monto())
                .pasajero(
                        Pasajero.builder()
                                .id(request.pasajeroId())
                                .build()
                )
                .conductor(
                        Conductor.builder()
                                .id(request.conductorId())
                                .build()
                ).viaje(
                        Viaje.builder()
                                .id(request.viajeId())
                                .build()
                ).build();
    }

    public PagoResponse toResponse(Pago pago) {
        if(pago == null){
            return null;
        }

        return new PagoResponse(
                pago.getId(),
                pago.getEstado(),
                pago.getFechaHoraPago(),
                pago.getMonto(),
                pago.getPasajero().getId(),
                pago.getConductor().getId(),
                pago.getViaje().getId()
        );
    }
}
