package com.interride.mapper;

import com.interride.dto.request.PasajeroViajeRequest;
import com.interride.dto.request.ViajeSolicitadoRequest;
import com.interride.dto.response.BoletoCompletadoResponse;
import com.interride.dto.response.BoletoResponse;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.PasajeroViaje;
import com.interride.model.entity.Ubicacion;
import com.interride.model.entity.Viaje;
import com.interride.model.enums.EstadoViaje;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PasajeroViajeMapper {
    public PasajeroViaje toEntity(PasajeroViajeRequest request){
        if(request == null) {
            return null;
        }

        return PasajeroViaje.builder()
                .asientosOcupados(request.asientosOcupados())
                .pasajero(
                        Pasajero.builder()
                                .id(request.idPasajero())
                                .build()
                )
                .viaje(
                        Viaje.builder()
                                .id(request.idViaje())
                                .build()
                )
                .abordo(false)
                .build();
    }

    public PasajeroViaje toEntity(ViajeSolicitadoRequest request){
        if(request == null) {
            return null;
        }

        return PasajeroViaje.builder()
                .fechaHoraUnion(LocalDateTime.now())
                .asientosOcupados(request.asientosReservados())
                .estado(EstadoViaje.SOLICITADO)
                .abordo(false)
                .build();
    }

    public BoletoCompletadoResponse toBoletoCompletadoResponse(PasajeroViaje boleto, String mensaje) {
        if(boleto == null) {
            return null;
        }
        return new BoletoCompletadoResponse(
                boleto.getId(),
                boleto.getFechaHoraLLegada().toString(),
                boleto.getCosto(),
                boleto.getEstado(),
                boleto.getPasajero().getId(),
                boleto.getViaje().getId(),
                mensaje
        );
    }

    public BoletoResponse toBoletoResponse(PasajeroViaje boleto, Ubicacion origen, Ubicacion destino) {
        if(boleto == null) {
            return null;
        }
        return new BoletoResponse(
                boleto.getId(),
                boleto.getViaje().getId(),
                boleto.getPasajero().getId(),
                boleto.getFechaHoraLLegada().toString(),
                boleto.getFechaHoraUnion().toString(),
                boleto.getAsientosOcupados(),
                boleto.getCosto(),
                boleto.getEstado().toString(),
                boleto.getAbordo(),
                origen.getProvincia(),
                origen.getDireccion(),
                destino.getProvincia(),
                destino.getDireccion()
        );
    }
}
