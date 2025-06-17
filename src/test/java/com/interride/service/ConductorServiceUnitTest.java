package com.interride.service;

import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.response.ConductorPerfilPublicoResponse;
import com.interride.dto.response.ConductorRegistroResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.DuplicateResourceException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.ConductorMapper;

import com.interride.model.entity.Conductor;
import com.interride.model.entity.Vehiculo;
import com.interride.model.entity.Viaje;
import com.interride.repository.ConductorRepository;
import com.interride.repository.ViajeRepository;
import com.interride.service.Impl.ConductorServiceImpl;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class ConductorServiceUnitTest {
    @Mock private ConductorRepository conductorRepository;
    @Mock private ConductorMapper conductorMapper;
    @Mock private ViajeRepository viajeRepository;

    @InjectMocks
    private ConductorServiceImpl conductorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS
    @Test
    @DisplayName("CP01 - Obtener perfil conductor asignado - Success")
    void testObtenerPerfilConductorAsignadoSuccess() {
        Integer idViaje = 1;
        Conductor conductor = new Conductor();
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca("ABC123");
        conductor.setId(1);
        conductor.setVehiculo(vehiculo);
        conductor.setNombre("John");
        conductor.setApellidos("Doe");
        conductor.setTelefono("123456789");
        Viaje viaje = new Viaje();
        viaje.setId(idViaje);
        viaje.setConductor(conductor);

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));


        ConductorPerfilPublicoResponse response = conductorService.obtenerPerfilConductorAsignado(idViaje);

        assertNotNull(response);
        assertEquals("John", response.nombres());
        assertEquals("Doe", response.apellidos());
        assertEquals("123456789", response.telefono());
        assertEquals("ABC123", response.placaVehiculo());
    }

    @Test
    @DisplayName("CP02 - Obtener perfil conductor asignado - Viaje no encontrado")
    void testObtenerPerfilConductorAsignadoViajeNoEncontrado() {
        Integer idViaje = 1;

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            conductorService.obtenerPerfilConductorAsignado(idViaje)
        );

        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP03 - Obtener perfil conductor asignado - Conductor no encontrado")
    void testObtenerPerfilConductorAsignadoConductorNoEncontrado() {
        Integer idViaje = 1;
        Viaje viaje = new Viaje();
        viaje.setId(idViaje);
        viaje.setConductor(null);

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
            conductorService.obtenerPerfilConductorAsignado(idViaje)
        );

        System.out.println(exception.getMessage());
    }
}
