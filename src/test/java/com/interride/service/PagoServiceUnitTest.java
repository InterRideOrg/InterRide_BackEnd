package com.interride.service;

import com.interride.dto.request.CreatePagoRequest;
import com.interride.dto.response.AnnualProfitReport;
import com.interride.dto.response.MonthlyProfitReport;
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
import java.util.ArrayList;
import java.util.List;
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

    @Test
    @DisplayName("CP01 - Obtener pagos por pasajero con éxito")
    void getPagosByPasajeroId_success_returnsPagoResponseList() {
        Integer pasajeroId = 1;
        Pago pago = new Pago();
        Pasajero pasajero = new Pasajero();
        pasajero.setId(pasajeroId);
        pago.setId(1);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setMonto(23.0);
        pago.setFechaHoraPago(LocalDateTime.now());
        when(pasajeroRepository.findById(pasajeroId)).thenReturn(Optional.of(pasajero));
        when(pagoRepository.findByPasajeroId(pasajeroId)).thenReturn(List.of(pago));
        when(pagoMapper.toResponse(pago)).thenReturn(new PagoResponse(
                pago.getId(),
                pago.getEstado(),
                pago.getFechaHoraPago(),
                pago.getMonto(),
                pasajeroId,
                1, // Conductor ID
                1  // Viaje ID
        ));

        List<PagoResponse> resultados = pagoService.getPagosByPasajeroId(pasajeroId);
        assertEquals(1, resultados.size());
        assertEquals(EstadoPago.PENDIENTE, resultados.getFirst().estado());
    }

    @Test
    @DisplayName("CP02 - Obtener pagos por pasajero con error de pasajero no encontrado")
    void getPagosByPasajeroId_error_pasajeroNotFound() {
        Integer pasajeroId = 1;

        when(pagoRepository.findByPasajeroId(pasajeroId)).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> pagoService.getPagosByPasajeroId(pasajeroId));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP03 - Completar pago con éxito")
    void completarPago_success() {
        Integer pagoId = 1;
        Pago pago = new Pago();
        pago.setId(pagoId);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setMonto(23.0);
        pago.setFechaHoraPago(LocalDateTime.now());

        Pasajero pasajero = new Pasajero();
        pasajero.setId(1);

        pago.setPasajero(pasajero);

        Notificacion notificacionConductor = new Notificacion();
        Notificacion notificacionPasajero = new Notificacion();

        PagoResponse expectedResponse = new PagoResponse(pagoId, EstadoPago.COMPLETADO, pago.getFechaHoraPago(), pago.getMonto(), pasajero.getId(), 1, 1);

        when(pagoRepository.findById(pagoId)).thenReturn(Optional.of(pago));
        when(pasajeroRepository.findById(1)).thenReturn(Optional.of(pasajero));
        when(notificacionRepository.save(notificacionConductor)).thenReturn(notificacionConductor);
        when(notificacionRepository.save(notificacionPasajero)).thenReturn(notificacionPasajero);
        when(pagoRepository.save(pago)).thenReturn(pago);
        when(pagoMapper.toResponse(pago)).thenReturn(expectedResponse);

        PagoResponse resultado = pagoService.completarPago(1);



        assertEquals(expectedResponse.estado(), resultado.estado());
        assertEquals(expectedResponse.monto(), resultado.monto());
        assertEquals(expectedResponse.pasajeroId(), resultado.pasajeroId());
        assertEquals(expectedResponse.id(), resultado.id());
    }

    @Test
    @DisplayName("CP04 - Completar pago con error de pago no encontrado")
    void completarPago_error_pagoNotFound() {
        Integer pagoId = 1;

        when(pagoRepository.findById(pagoId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> pagoService.completarPago(pagoId));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP05 - Completar pago con error de pasajero no encontrado")
    void completarPago_error_pasajeroNotFound() {
        Integer pagoId = 1;
        Pago pago = new Pago();
        pago.setId(pagoId);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setMonto(23.0);
        pago.setFechaHoraPago(LocalDateTime.now());
        Pasajero pasajero = new Pasajero();
        pasajero.setId(1);
        pago.setPasajero(pasajero);

        when(pagoRepository.findById(pagoId)).thenReturn(Optional.of(pago));
        when(pasajeroRepository.findById(pago.getPasajero().getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> pagoService.completarPago(pagoId));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP06 - Completar pago con error de estado no válido")
    void completarPago_error_estadoNoValido() {
        Integer pagoId = 1;
        Pago pago = new Pago();
        pago.setId(pagoId);
        pago.setEstado(EstadoPago.COMPLETADO); // Estado no válido para completar
        pago.setMonto(23.0);
        pago.setFechaHoraPago(LocalDateTime.now());
        Pasajero pasajero = new Pasajero();
        pasajero.setId(1);
        pago.setPasajero(pasajero);

        when(pagoRepository.findById(pagoId)).thenReturn(Optional.of(pago));
        when(pasajeroRepository.findById(pago.getPasajero().getId())).thenReturn(Optional.of(pasajero));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> pagoService.completarPago(pagoId));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP07 - Obtener Reporte de ganancias anuales por conductor con éxito")
    void getAnnualProfitReportByConductor_success() {
        Integer year = 2023;
        Integer conductorId = 1;

        Conductor conductor = new Conductor();
        conductor.setId(conductorId);


        Object[] elem = new Object[]{1, 87}; // mes, total

        List<Object[]> reportData = new ArrayList<>();
        reportData.add(elem);

        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(conductor));
        when(pagoRepository.findPagosByYearGroupedByMonth(year, conductorId)).thenReturn(reportData);

        List<AnnualProfitReport> resultados = pagoService.getAnnualProfitReportByConductor(year, conductorId);
        assertEquals(1, resultados.size());
        assertEquals(1, resultados.getFirst().mes());
        assertEquals(87.0, resultados.getFirst().totalGanancias());
    }

    @Test
    @DisplayName("CP08 - Obtener Reporte de ganancias anuales por conductor con error de conductor no encontrado")
    void getAnnualProfitReportByConductor_error_conductorNotFound() {
        Integer year = 2023;
        Integer conductorId = 1;

        when(conductorRepository.findById(conductorId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> pagoService.getAnnualProfitReportByConductor(year, conductorId));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("CP09 - Obtener Reporte de ganancias mensuales por conductor con éxito")
    void getMonthlyProfitReportByConductor_success() {
        Integer year = 2023;
        Integer month = 5;
        Integer conductorId = 1;

        Conductor conductor = new Conductor();
        conductor.setId(conductorId);

        Object[] elem = new Object[]{5, 150}; // mes, total

        List<Object[]> reportData = new ArrayList<>();
        reportData.add(elem);

        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(conductor));
        when(pagoRepository.findPagosByMonthGroupedByDay(year, month, conductorId)).thenReturn(reportData);
        List<MonthlyProfitReport> resultados = pagoService.getMonthlyProfitReportByConductor(year, month, conductorId);
        assertEquals(1, resultados.size());
        assertEquals(5, resultados.getFirst().dia());
        assertEquals(150.0, resultados.getFirst().totalGanancias());

    }

    @Test
    @DisplayName("CP10 - Obtener Reporte de ganancias mensuales por conductor con error de conductor no encontrado")
    void getMonthlyProfitReportByConductor_error_conductorNotFound() {
        Integer year = 2023;
        Integer month = 5;
        Integer conductorId = 1;

        when(conductorRepository.findById(conductorId
        )).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> pagoService.getMonthlyProfitReportByConductor(year, month, conductorId));
        System.out.println(exception.getMessage());
    }


}
