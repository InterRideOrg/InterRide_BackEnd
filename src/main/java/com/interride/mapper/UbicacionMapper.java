package com.interride.mapper;

import com.interride.dto.request.ViajeSolicitadoRequest;
import com.interride.dto.request.UbicacionRequest;
import com.interride.model.entity.Ubicacion;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class UbicacionMapper {
    public Ubicacion toEntity(UbicacionRequest request){
        if(request == null) {
            return null;
        }

        return Ubicacion.builder()
                .latitud(request.latitud())
                .longitud(request.longitud())
                .direccion(request.direccion())
                .provincia(request.provincia())
                .build();
    }

    public Pair<Ubicacion, Ubicacion> OrigenDestinotoEntity(ViajeSolicitadoRequest request){
        if(request == null) {
            return null;
        }

        Ubicacion origen = Ubicacion.builder()
                .latitud(request.latitudOrigen())
                .longitud(request.longitudOrigen())
                .direccion(request.direccionOrigen())
                .provincia(request.provinciaOrigen())
                .build();

        Ubicacion destino = Ubicacion.builder()
                .latitud(request.latitudDestino())
                .longitud(request.longitudDestino())
                .direccion(request.direccionDestino())
                .provincia(request.provinciaDestino())
                .build();

        return Pair.of(origen, destino);
    }
}
