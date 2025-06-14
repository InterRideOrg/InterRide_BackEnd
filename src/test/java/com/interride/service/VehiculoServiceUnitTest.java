package com.interride.service;

import com.interride.dto.request.VehiculoRequest;
import com.interride.dto.response.VehiculoResponse;
import com.interride.exception.DuplicateResourceException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.VehiculoMapper;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Vehiculo;
import com.interride.repository.ConductorRepository;
import com.interride.repository.VehiculoRepository;
import com.interride.service.Impl.VehiculoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class VehiculoServiceUnitTest {
    @Mock private VehiculoRepository vehiculoRepository;
    @Mock private ConductorRepository conductorRepository;
    @Mock private VehiculoMapper vehiculoMapper;

    @InjectMocks
    private VehiculoServiceImpl vehiculoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS

    @Test
    @DisplayName("HU08 - CP01 - Actualización exitosa de vehículo")
    void updateVehiculo_success_returnsVehiculoResponse() {
        Integer conductorId = 1;
        Conductor conductor = Conductor.builder().id(conductorId).build();
        Vehiculo vehiculoExistente = Vehiculo.builder()
                .id(10)
                .placa("OLD123")
                .conductor(conductor)
                .build();

        VehiculoRequest request = new VehiculoRequest("NEW456", "Toyota", "Corolla", 2022, 4);

        VehiculoResponse responseEsperado = new VehiculoResponse(
                "NEW456", "Toyota", "Corolla", 2022, 4
        );

        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(conductor));
        when(vehiculoRepository.findByConductorId(conductorId)).thenReturn(Optional.of(vehiculoExistente));
        when(vehiculoRepository.existsByPlacaAndIdNot("NEW456", 10)).thenReturn(false);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(vehiculoMapper.toResponse(any(Vehiculo.class))).thenReturn(responseEsperado);

        VehiculoResponse resultado = vehiculoService.update(conductorId, request);

        assertNotNull(resultado);
        assertEquals("NEW456", resultado.placa());
        assertEquals("Toyota", resultado.marca());
        assertEquals("Corolla", resultado.modelo());
        assertEquals(2022, resultado.anio());
        assertEquals(4, resultado.cantidadAsientos());
    }

    @Test
    @DisplayName("HU08 - CP02 - Vehículo no encontrado lanza excepción")
    void updateVehiculo_notFound_throwsResourceNotFoundException() {
        Integer conductorId = 1;
        VehiculoRequest request = new VehiculoRequest("XYZ789", "Toyota", "Corolla", 2022, 4);

        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(Conductor.builder().id(conductorId).build()));
        when(vehiculoRepository.findByConductorId(conductorId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> vehiculoService.update(conductorId, request)
        );

        assertEquals("Vehículo no encontrado para el conductor", ex.getMessage());
    }

    @Test
    @DisplayName("HU08 - CP03 - Conductor no encontrado lanza excepción")
    void updateVehiculo_conductorNotFound_throwsResourceNotFoundException() {
        Integer conductorId = 1;
        VehiculoRequest request = new VehiculoRequest("XYZ789", "Toyota", "Corolla", 2022, 4);

        when(conductorRepository.findById(conductorId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> vehiculoService.update(conductorId, request)
        );

        assertEquals("Conductor no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("HU08 - CP04 - Placa duplicada lanza excepción")
    void updateVehiculo_placaDuplicada_throwsDuplicateResourceException() {
        Integer conductorId = 1;
        Conductor conductor = Conductor.builder().id(conductorId).build();
        Vehiculo vehiculoExistente = Vehiculo.builder()
                .id(10)
                .placa("OLD123")
                .conductor(conductor)
                .build();

        VehiculoRequest request = new VehiculoRequest("XYZ789", "Toyota", "Corolla", 2022, 4);

        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(conductor));
        when(vehiculoRepository.findByConductorId(conductorId)).thenReturn(Optional.of(vehiculoExistente));
        when(vehiculoRepository.existsByPlacaAndIdNot("XYZ789", 10)).thenReturn(true);

        DuplicateResourceException ex = assertThrows(
                DuplicateResourceException.class,
                () -> vehiculoService.update(conductorId, request)
        );

        assertEquals("Placa ya registrada por otro conductor. Ingrese otro.", ex.getMessage());
    }
}
