package com.interride.mapper;


import com.interride.dto.request.VehiculoRequest;
import com.interride.dto.response.VehiculoResponse;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Vehiculo;
import org.springframework.stereotype.Component;

@Component
public class VehiculoMapper {


    public VehiculoResponse toResponse(Vehiculo vehiculo) {
        return new VehiculoResponse(
                vehiculo.getPlaca(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAnio(),
                vehiculo.getCantidadAsientos()
        );
    }

}
