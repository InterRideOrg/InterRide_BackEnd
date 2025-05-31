package com.interride.service.Impl;

import com.interride.dto.request.RegistroDeVehiculoRequest;
import com.interride.repository.ConductorRepository;
import com.interride.repository.VehiculoRepository;
import com.interride.model.entity.Vehiculo;
import com.interride.service.VehiculoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ConductorRepository conductorRepository;

    @Transactional
    @Override
    public Vehiculo update(Integer conductorId, Vehiculo vehiculoNuevo) {
        Vehiculo vehiculo = vehiculoRepository.findByConductorId(conductorId)
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado para el conductor"));

        vehiculo.setPlaca(vehiculoNuevo.getPlaca());
        vehiculo.setMarca(vehiculoNuevo.getMarca());
        vehiculo.setModelo(vehiculoNuevo.getModelo());
        vehiculo.setAnio(vehiculoNuevo.getAnio());
        vehiculo.setCantidadAsientos(vehiculoNuevo.getCantidadAsientos());

        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    @Override
    public Vehiculo registrar(Integer conductorId, RegistroDeVehiculoRequest registroDeVehiculoRequest) {
        // Verificar que el conductor no tenga vehiculo registrado
        if (vehiculoRepository.existsByConductorId(conductorId)) {
            throw new IllegalStateException("El conductor ya tiene un vehículo registrado.");
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(registroDeVehiculoRequest.getPlaca());
        vehiculo.setMarca(registroDeVehiculoRequest.getMarca());
        vehiculo.setModelo(registroDeVehiculoRequest.getModelo());
        vehiculo.setAnio(registroDeVehiculoRequest.getAnio());
        vehiculo.setCantidadAsientos(registroDeVehiculoRequest.getCantidadAsientos());

        // Asignar el conductor al vehículo
        vehiculo.setConductor(conductorRepository.findById(conductorId)
                .orElseThrow(() -> new EntityNotFoundException("Conductor no encontrado con ID: " + conductorId)));
        // Guardar el vehículo en la base de datos
        return vehiculoRepository.save(vehiculo);
    }

}
