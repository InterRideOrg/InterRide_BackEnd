package com.interride.mapper;

import com.interride.dto.request.UbicacionRequest;
import com.interride.model.entity.Ubicacion;
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
}
