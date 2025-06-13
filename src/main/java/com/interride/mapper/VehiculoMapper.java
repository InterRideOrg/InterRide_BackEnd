package com.interride.mapper;


import com.interride.dto.request.UpdateVehiculoRequest;
import com.interride.model.entity.Vehiculo;
import org.springframework.stereotype.Component;

@Component
public class VehiculoMapper {
    public void updateEntity(Vehiculo vehiculo, UpdateVehiculoRequest request) {
        vehiculo.setPlaca(request.placa());
        vehiculo.setMarca(request.marca());
        vehiculo.setModelo(request.modelo());
        vehiculo.setAnio(request.anio());
        vehiculo.setCantidadAsientos(request.cantidadAsientos());
    }

}
