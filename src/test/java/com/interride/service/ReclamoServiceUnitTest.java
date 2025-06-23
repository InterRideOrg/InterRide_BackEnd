package com.interride.service;

import com.interride.dto.request.ReclamoRequest;
import com.interride.dto.response.ReclamoResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Reclamo;
import com.interride.repository.ConductorRepository;
import com.interride.repository.PasajeroRepository;
import com.interride.repository.ReclamoRepository;
import com.interride.service.Impl.ReclamoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ReclamoServiceUnitTest {
    @Mock private ReclamoRepository reclamoRepository;
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private ConductorRepository conductorRepository;

    @InjectMocks
    private ReclamoServiceImpl reclamoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS

    @Test
    @DisplayName("CP01 - Crear reclamo exitosamente para un conductor")
    void crearReclamo_conductor_success() {
        Conductor conductor = Conductor.builder().id(1).nombre("Carlos").build();
        ReclamoRequest request = new ReclamoRequest("Mensaje prueba", null, 1);
        Reclamo reclamo = new Reclamo();
        reclamo.setId(10);
        reclamo.setMensaje(request.mensaje());
        reclamo.setFechaHoraEnvio(LocalDateTime.now());
        reclamo.setConductor(conductor);

        when(conductorRepository.findById(1)).thenReturn(Optional.of(conductor));
        when(reclamoRepository.save(any(Reclamo.class))).thenAnswer(i -> {
            Reclamo r = i.getArgument(0);
            r.setId(10);
            return r;
        });

        ReclamoResponse response = reclamoService.crearReclamo(request);

        assertNotNull(response);
        assertEquals(10, response.id());
        assertEquals("Reclamo enviado. Nos comunicaremos en 24h.", response.mensaje());
        assertNotNull(response.fechaHoraEnvio());
    }

    @Test
    @DisplayName("CP02 - Crear reclamo exitosamente para un pasajero")
    void crearReclamo_pasajero_success() {
        Pasajero pasajero = Pasajero.builder().id(2).nombre("Carlos").build();
        ReclamoRequest request = new ReclamoRequest("Mensaje prueba", 2, null);
        Reclamo reclamo = new Reclamo();
        reclamo.setId(11);
        reclamo.setMensaje(request.mensaje());
        reclamo.setFechaHoraEnvio(LocalDateTime.now());
        reclamo.setPasajero(pasajero);

        when(pasajeroRepository.findById(2)).thenReturn(Optional.of(pasajero));
        when(reclamoRepository.save(any(Reclamo.class))).thenAnswer(i -> {
            Reclamo r = i.getArgument(0);
            r.setId(11);
            return r;
        });

        ReclamoResponse response = reclamoService.crearReclamo(request);

        assertNotNull(response);
        assertEquals(11, response.id());
        assertEquals("Reclamo enviado. Nos comunicaremos en 24h.", response.mensaje());
        assertNotNull(response.fechaHoraEnvio());
    }

    @Test
    @DisplayName("CP03 - Fallo por mensaje vacío")
    void crearReclamo_mensajeVacio_throwsException() {
        ReclamoRequest request = new ReclamoRequest("   ", 1, null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> reclamoService.crearReclamo(request));

        assertEquals("El mensaje no puede estar vacío.", ex.getMessage());
    }

    @Test
    @DisplayName("CP04 - Fallo por no enviar ID de pasajero ni conductor")
    void crearReclamo_sinId_throwsException() {
        ReclamoRequest request = new ReclamoRequest("Mensaje prueba", null, null);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> reclamoService.crearReclamo(request));

        assertEquals("Debe especificar algun ID.", ex.getMessage());
    }

    @Test
    @DisplayName("CP05 - Fallo por conductor no encontrado")
    void crearReclamo_conductorNoExiste_throwsException() {
        ReclamoRequest request = new ReclamoRequest("Mensaje prueba", null, 99);
        when(conductorRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> reclamoService.crearReclamo(request));

        assertEquals("Conductor no encontrado.", ex.getMessage());
    }

    @Test
    @DisplayName("CP06 - Fallo por pasajero no encontrado")
    void crearReclamo_pasajeroNoExiste_throwsException() {
        ReclamoRequest request = new ReclamoRequest("Mensaje prueba", 99, null);
        when(pasajeroRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> reclamoService.crearReclamo(request));

        assertEquals("Pasajero no encontrado.", ex.getMessage());
    }
}
