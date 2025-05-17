package com.interride.service.Impl;

import com.interride.model.entity.Calificacion;
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

}
