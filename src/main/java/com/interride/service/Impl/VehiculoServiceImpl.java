package com.interride.service.Impl;

import com.interride.dto.request.RegistroDeVehiculoRequest;
import com.interride.dto.request.VehiculoRequest;
import com.interride.dto.response.VehiculoResponse;
import com.interride.exception.DuplicateResourceException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.VehiculoMapper;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Usuario;
import com.interride.model.enums.ERole;
import com.interride.repository.ConductorRepository;
import com.interride.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;
    private final VehiculoMapper vehiculoMapper;

    @Transactional
    @Override
    public VehiculoResponse update(Integer conductorId, VehiculoRequest request) {
        Conductor conductor = conductorRepository.findById(conductorId)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado"));

        // Busca vehículo asociado
        Vehiculo vehiculo = vehiculoRepository.findByConductorId(conductorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado para el conductor"));

        // Verifica si la placa ya existe
        if (vehiculoRepository.existsByPlacaAndIdNot(request.placa(), vehiculo.getId())) {
            throw new DuplicateResourceException("Placa ya registrada por otro conductor. Ingrese otro.");
        }

        vehiculo.setPlaca(request.placa());
        vehiculo.setMarca(request.marca());
        vehiculo.setModelo(request.modelo());
        vehiculo.setAnio(request.anio());
        vehiculo.setCantidadAsientos(request.cantidadAsientos());

        // Guardar el vehículo actualizado
        Vehiculo actualizado = vehiculoRepository.save(vehiculo);

        // Retornar un DTO de respuesta (puedes mantener esto en el mapper)
        return vehiculoMapper.toResponse(actualizado);
    }


    @Transactional
    @Override
    public VehiculoResponse registrar(Integer usuarioId, RegistroDeVehiculoRequest registroDeVehiculoRequest) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        if(usuario.getRole().getNombre() != ERole.CONDUCTOR) {
            throw new IllegalStateException("El usuario no es un conductor.");
        }

        Conductor conductor = usuario.getConductor();

        // Verificar que el conductor no tenga vehiculo registrado
        if (vehiculoRepository.existsByConductorId(conductor.getId())) {
            throw new IllegalStateException("El conductor ya tiene un vehículo registrado.");
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(registroDeVehiculoRequest.getPlaca());
        vehiculo.setMarca(registroDeVehiculoRequest.getMarca());
        vehiculo.setModelo(registroDeVehiculoRequest.getModelo());
        vehiculo.setAnio(registroDeVehiculoRequest.getAnio());
        vehiculo.setCantidadAsientos(registroDeVehiculoRequest.getCantidadAsientos());

        // Asignar el conductor al vehículo
        vehiculo.setConductor(conductorRepository.findById(conductor.getId())
                .orElseThrow(() -> new EntityNotFoundException("Conductor no encontrado con ID: " + conductor.getId())));
        // Guardar el vehículo en la base de datos
        vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toResponse(vehiculo);
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculoResponse obtenerVehiculoPorConductorId(Integer conductorId) {
        Vehiculo vehiculo = vehiculoRepository.findByConductorId(conductorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado para el conductor con ID: " + conductorId));
        return vehiculoMapper.toResponse(vehiculo);
    }

}