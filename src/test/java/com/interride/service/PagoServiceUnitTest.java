package com.interride.service;

import com.interride.dto.request.CreatePagoRequest;
import com.interride.dto.response.PagoResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.PagoMapper;
import com.interride.model.entity.*;
import com.interride.model.enums.EstadoPago;
import com.interride.repository.*;
import com.interride.service.Impl.PagoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.MockitoAnnotations;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class PagoServiceUnitTest {
    @Mock private PagoMapper pagoMapper;
    @Mock private PagoRepository pagoRepository;
    @Mock private TarjetaRepository tarjetaRepository;
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private ConductorRepository conductorRepository;
    @Mock private NotificacionRepository notificacionRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS
    @Test
    @DisplayName("UH17 - CP01 - Crear pago con tarjeta con éxito")
    void createPagoTarjeta_success_returnsPagoResponse() {
        Integer tarjetaId = 1;
        Integer pasajeroId = 1;
        Integer conductorId = 1;
        Integer viajeId = 1;
        CreatePagoRequest request = new CreatePagoRequest(
                23.0,
                pasajeroId,
                conductorId,
                viajeId
        );

        Pasajero pasajero = new Pasajero();
        pasajero.setId(pasajeroId);

        Conductor conductor = new Conductor();
        conductor.setId(conductorId);

        Viaje viaje = new Viaje();
        viaje.setId(viajeId);

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(tarjetaId);
        tarjeta.setPasajero(pasajero);

        Pago pago = new Pago();
        pago.setId(1);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setMonto(23.0);
        pago.setFechaHoraPago(LocalDateTime.now());
        pago.setPasajero(pasajero);
        pago.setConductor(conductor);
        pago.setViaje(viaje);

        when(pagoMapper.toEntity(request)).thenReturn(pago);
        when(tarjetaRepository.findById(tarjetaId)).thenReturn(Optional.of(tarjeta));
        when(pasajeroRepository.findById(pasajeroId)).thenReturn(Optional.of(pasajero));
        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(conductor));

        when(pagoRepository.save(pago)).thenReturn(pago);
        when(pagoMapper.toResponse(pago)).thenReturn(new PagoResponse(
                pago.getId(),
                pago.getEstado(),
                pago.getFechaHoraPago(),
                pago.getMonto(),
                pasajeroId,
                conductorId,
                viajeId
        ));
        PagoResponse resultado = pagoService.createPagoTarjeta(request, tarjetaId);

        assertEquals(EstadoPago.PENDIENTE, resultado.estado());
        assertEquals(23.0, resultado.monto());
        assertEquals(pasajeroId, resultado.pasajeroId());
        assertEquals(conductorId, resultado.conductorId());
        assertEquals(viajeId, resultado.viajeId());
    }

    @Test
    @DisplayName("UH17 - CP02 - Crear pago con tarjeta con error de tarjeta no encontrada")
    void createPagoTarjeta_error_tarjetaNotFound() {
        Integer tarjetaId = 1;
        CreatePagoRequest request = new CreatePagoRequest(23.0, 1, 1, 1);
        Pago pago = new Pago();

        when(pagoMapper.toEntity(request)).thenReturn(pago);
        when(tarjetaRepository.findById(tarjetaId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> pagoService.createPagoTarjeta(request, tarjetaId));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("UH17 - CP03 - Crear pago con tarjeta con error de pasajero no encontrado")
    void createPagoTarjeta_error_pasajeroNotFound() {
        Integer tarjetaId = 1;
        CreatePagoRequest request = new CreatePagoRequest(23.0, 1, 1, 1);

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(tarjetaId);

        Pasajero pasajero = new Pasajero();
        pasajero.setId(request.pasajeroId());

        Pago pago = new Pago();
        pago.setId(1);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setPasajero(pasajero);


        when(pagoMapper.toEntity(request)).thenReturn(pago);
        when(tarjetaRepository.findById(tarjetaId)).thenReturn(Optional.of(tarjeta));
        when(pasajeroRepository.findById(request.pasajeroId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> pagoService.createPagoTarjeta(request, tarjetaId));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("UH17 - CP04 - Crear pago con tarjeta con error de conductor no encontrado")
    void createPagoTarjeta_error_conductorNotFound() {
        Integer tarjetaId = 1;
        CreatePagoRequest request = new CreatePagoRequest(23.0, 1, 1, 1);

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(tarjetaId);

        Pasajero pasajero = new Pasajero();
        pasajero.setId(request.pasajeroId());

        Conductor conductor = new Conductor();
        conductor.setId(request.conductorId());

        Pago pago = new Pago();
        pago.setId(1);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setPasajero(pasajero);
        pago.setConductor(conductor);

        when(pagoMapper.toEntity(request)).thenReturn(pago);
        when(tarjetaRepository.findById(tarjetaId)).thenReturn(Optional.of(tarjeta));
        when(pasajeroRepository.findById(request.pasajeroId())).thenReturn(Optional.of(new Pasajero()));
        when(conductorRepository.findById(request.conductorId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> pagoService.createPagoTarjeta(request, tarjetaId));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("UH17 - CP05 - Crear pago con tarjeta con error de pasajero no le pertenece la tarjeta")
    void createPagoTarjeta_error_pasajeroNoPerteneceTarjeta() {
        Integer tarjetaId = 1;
        CreatePagoRequest request = new CreatePagoRequest(23.0, 1, 1, 1);

        Pasajero pasajeroPropietario = new Pasajero();
        pasajeroPropietario.setId(2); // Tarjeta pertenece a otro pasajero

        Conductor conductor = new Conductor();
        conductor.setId(request.conductorId());

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(tarjetaId);
        tarjeta.setPasajero(pasajeroPropietario); // Tarjeta pertenece a otro pasajero

        Pasajero pasajero = new Pasajero();
        pasajero.setId(request.pasajeroId());

        Pago pago = new Pago();
        pago.setId(1);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setPasajero(pasajero);
        pago.setConductor(conductor);

        when(pagoMapper.toEntity(request)).thenReturn(pago);
        when(tarjetaRepository.findById(tarjetaId)).thenReturn(Optional.of(tarjeta));
        when(pasajeroRepository.findById(request.pasajeroId())).thenReturn(Optional.of(pasajero));
        when(conductorRepository.findById(request.conductorId())).thenReturn(Optional.of(new Conductor()));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> pagoService.createPagoTarjeta(request, tarjetaId));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("UH18 - CP01 - Crear pago en efectivo con éxito")
    void createPagoEfectivo_success_returnsPagoResponse() {
        Integer pasajeroId = 1;
        Integer conductorId = 1;
        Integer viajeId = 1;
        CreatePagoRequest request = new CreatePagoRequest(
                23.0,
                pasajeroId,
                conductorId,
                viajeId
        );

        Pasajero pasajero = new Pasajero();
        pasajero.setId(pasajeroId);

        Conductor conductor = new Conductor();
        conductor.setId(conductorId);

        Viaje viaje = new Viaje();
        viaje.setId(viajeId);

        Pago pago = new Pago();
        pago.setId(1);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setMonto(23.0);
        pago.setFechaHoraPago(LocalDateTime.now());
        pago.setPasajero(pasajero);
        pago.setConductor(conductor);
        pago.setViaje(viaje);

        when(pagoMapper.toEntity(request)).thenReturn(pago);
        when(pasajeroRepository.findById(pasajeroId)).thenReturn(Optional.of(pasajero));
        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(conductor));
        when(pagoRepository.save(pago)).thenReturn(pago);
        when(pagoMapper.toResponse(pago)).thenReturn(new PagoResponse(
                pago.getId(),
                pago.getEstado(),
                pago.getFechaHoraPago(),
                pago.getMonto(),
                pasajeroId,
                conductorId,
                viajeId
        ));
        PagoResponse resultado = pagoService.createPagoEfectivo(request);
        assertEquals(EstadoPago.PENDIENTE, resultado.estado());
        assertEquals(23.0, resultado.monto());
        assertEquals(pasajeroId, resultado.pasajeroId());
        assertEquals(conductorId, resultado.conductorId());
        assertEquals(viajeId, resultado.viajeId());
    }

    @Test
    @DisplayName("UH18 - CP02 - Crear pago en efectivo con error de pasajero no encontrado")
    void createPagoEfectivo_error_pasajeroNotFound() {
        Integer pasajeroId = 1;
        Integer conductorId = 1;
        Integer viajeId = 1;
        CreatePagoRequest request = new CreatePagoRequest(
                23.0,
                pasajeroId,
                conductorId,
                viajeId
        );

        Conductor conductor = new Conductor();
        conductor.setId(conductorId);

        Viaje viaje = new Viaje();
        viaje.setId(viajeId);

        Pago pago = new Pago();
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setMonto(23.0);
        pago.setFechaHoraPago(LocalDateTime.now());
        pago.setConductor(conductor);
        pago.setPasajero(Pasajero.builder().id(1000).build());
        pago.setViaje(viaje);

        when(pagoMapper.toEntity(request)).thenReturn(pago);
        when(pasajeroRepository.findById(pasajeroId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> pagoService.createPagoEfectivo(request));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("UH18 - CP03 - Crear pago en efectivo con error de conductor no encontrado")
    void createPagoEfectivo_error_conductorNotFound() {
        Integer pasajeroId = 1;
        Integer conductorId = 1;
        Integer viajeId = 1;
        CreatePagoRequest request = new CreatePagoRequest(
                23.0,
                pasajeroId,
                conductorId,
                viajeId
        );

        Pasajero pasajero = new Pasajero();
        pasajero.setId(pasajeroId);

        Viaje viaje = new Viaje();
        viaje.setId(viajeId);

        Pago pago = new Pago();
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setMonto(23.0);
        pago.setFechaHoraPago(LocalDateTime.now());
        pago.setPasajero(pasajero);
        pago.setConductor(Conductor.builder().id(1000).build());
        pago.setViaje(viaje);

        when(pagoMapper.toEntity(request)).thenReturn(pago);
        when(pasajeroRepository.findById(pasajeroId)).thenReturn(Optional.of(pasajero));
        when(conductorRepository.findById(conductorId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> pagoService.createPagoEfectivo(request));
        System.out.println(exception.getMessage());
    }


}
