package com.interride.service;

import com.interride.dto.VehiculoRequest;
import com.interride.dto.VehiculoResponse;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Vehiculo;
import com.interride.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehiculoService {
    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    public VehiculoResponse registrarVehiculo(VehiculoRequest request) {
        if (vehiculoRepository.existsByPlaca(request.getPlaca())) {
            throw new IllegalArgumentException("Ya existe un vehículo con esa placa.");
        }

        Conductor conductor = conductorRepository.findById(request.getConductorId())
                .orElseThrow(() -> new IllegalArgumentException("Conductor no encontrado."));

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(request.getPlaca());
        vehiculo.setMarca(request.getMarca());
        vehiculo.setModelo(request.getModelo());
        vehiculo.setAnio(request.getAnio());
        vehiculo.setCantidadAsientos(request.getCantidadAsientos());
        vehiculo.setConductor(conductor);

        Vehiculo guardado = vehiculoRepository.save(vehiculo);

        VehiculoResponse response = new VehiculoResponse();
        response.setId(guardado.getId());
        response.setPlaca(guardado.getPlaca());
        response.setMarca(guardado.getMarca());
        response.setModelo(guardado.getModelo());
        response.setAnio(guardado.getAnio());
        response.setCantidadAsientos(guardado.getCantidadAsientos());
        response.setConductorId(conductor.getId());
        response.setNombreConductor(conductor.getNombre() + " " + conductor.getApellidos());

        return response;
    }
}
