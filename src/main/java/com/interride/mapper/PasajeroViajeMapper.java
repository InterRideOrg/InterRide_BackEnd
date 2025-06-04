package com.interride.mapper;

import com.interride.dto.request.PasajeroViajeRequest;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.PasajeroViaje;
import com.interride.model.entity.Viaje;
import org.springframework.stereotype.Component;

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
                .build();
    }
}
