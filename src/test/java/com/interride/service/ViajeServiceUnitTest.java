package com.interride.service;

import com.interride.dto.request.ViajeSolicitadoRequest;
import com.interride.dto.response.PasajeroViajesResponse;
import com.interride.dto.response.ViajeSolicitadoResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.PasajeroViajeMapper;
import com.interride.mapper.UbicacionMapper;
import com.interride.mapper.ViajeMapper;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.PasajeroViaje;
import com.interride.model.entity.Ubicacion;
import com.interride.model.entity.Viaje;
import com.interride.model.enums.EstadoViaje;
import com.interride.repository.*;
import com.interride.service.Impl.ViajeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ViajeServiceUnitTest {
    @Mock private ViajeRepository viajeRepository;
    @Mock private NotificacionRepository notificacionRepository;
    @Mock private ConductorRepository conductorRepository;
    @Mock private PasajeroViajeRepository pasajeroViajeRepository;
    @Mock private UbicacionRepository ubicacionRepository;
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private CalificacionRepository calificacionRepository;
    @Mock private ViajeMapper viajeMapper;
    @Mock private UbicacionMapper ubicacionMapper;
    @Mock private PasajeroViajeMapper pasajeroViajeMapper;

    @InjectMocks
    private ViajeServiceImpl viajeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS

    @Test
    @DisplayName("UH10 - CP01 - Solicitar viaje con exito")
    void crearViajeSolicitado_success_returnsCreated() {
        Integer pasajeroId = 1;
        Pasajero pasajero = Pasajero.builder().id(pasajeroId).build();
        Viaje viaje = Viaje.builder().build();
        Ubicacion origen = Ubicacion.builder()
                .latitud(BigDecimal.valueOf(-12.04637300))
                .longitud(BigDecimal.valueOf(-77.04275400))
                .direccion("Av. Arequipa 123")
                .provincia("Lima")
                .build();

        Ubicacion destino = Ubicacion.builder()
                .latitud(BigDecimal.valueOf(-10.87654300))
                .longitud(BigDecimal.valueOf(-77.65432100))
                .direccion("Jr. Lima 234")
                .provincia("Haura")
                .build();

        Pair<Ubicacion, Ubicacion> ubicaciones = Pair.of(origen, destino);
        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(1)
                .fechaHoraUnion(LocalDateTime.now())
                .asientosOcupados(2)
                .estado(EstadoViaje.SOLICITADO)
                .pasajero(pasajero)
                .viaje(viaje)
                .fechaHoraLLegada(LocalDateTime.now().plusDays(3))
                .costo(25.0)
                .abordo(false)
                .build();


        ViajeSolicitadoRequest request = new ViajeSolicitadoRequest(
                "2025-05-31 17:02:40.728967",
                2,
                BigDecimal.valueOf(-12.04637300),
                BigDecimal.valueOf(-77.04275400),
                "Lima",
                "Av. Arequipa 123",
                BigDecimal.valueOf(-10.87654300),
                BigDecimal.valueOf(-77.65432100),
                "Haura",
                "Jr. Lima 234"
        );

        when(pasajeroRepository.findById(pasajeroId)).thenReturn(Optional.of(pasajero));
        when(viajeMapper.toEntity(request)).thenReturn(viaje);
        when(ubicacionMapper.OrigenDestinotoEntity(request)).thenReturn(ubicaciones);
        when(pasajeroViajeMapper.toEntity(request)).thenReturn(boleto);
        when(viajeRepository.save(viaje)).thenReturn(viaje);
        when(pasajeroViajeRepository.save(boleto)).thenReturn(boleto);
        when(ubicacionRepository.save(origen)).thenReturn(origen);
        when(ubicacionRepository.save(destino)).thenReturn(destino);

        ViajeSolicitadoResponse response = new ViajeSolicitadoResponse(
                1,
                "2025-05-31 17:02:40.728967",
                "2025-06-03 17:02:40.728967",
                "Lima",
                "Haura",
                "Av. Arequipa 123",
                "Jr. Lima 234",
                25.0,
                2,
                EstadoViaje.SOLICITADO.toString()
        );

        when(viajeMapper.toViajeSolicitadoResponse(viaje, boleto, origen, destino)).thenReturn(response);

        ViajeSolicitadoResponse resultado = viajeService.crearViajeSolicitado(pasajeroId, request);

        assertEquals("Lima", resultado.provinciaOrigen());
        assertEquals( "Haura", resultado.provinciaDestino());
        assertEquals("Av. Arequipa 123", resultado.direccionOrigen());
        assertEquals("Jr. Lima 234", resultado.direccionDestino());
        assertEquals(25.0, resultado.costo());
        assertEquals(2, resultado.asientosReservados());
    }

    @Test
    @DisplayName("UH10 - CP02 - Solicitar viaje con pasajero no encontrado")
    void crearViajeSolicitado_pasajeroNotFound_throwsException() {
        Integer pasajeroId = 1;
        ViajeSolicitadoRequest request = new ViajeSolicitadoRequest(
                "2025-05-31 17:02:40.728967",
                2,
                BigDecimal.valueOf(-12.04637300),
                BigDecimal.valueOf(-77.04275400),
                "Lima",
                "Av. Arequipa 123",
                BigDecimal.valueOf(-10.87654300),
                BigDecimal.valueOf(-77.65432100),
                "Haura",
                "Jr. Lima 234"
        );

        when(pasajeroRepository.findById(pasajeroId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> viajeService.crearViajeSolicitado(pasajeroId, request));
    }

    @Test
    @DisplayName("UH10 - CP03 - Solicitar viaje con asientos negativos o cero")
    void crearViajeSolicitado_invalidAsientos_throwsException() {
        Integer pasajeroId = 1;
        Pasajero pasajero = Pasajero.builder().id(pasajeroId).build();
        Viaje viaje = Viaje.builder().build();
        Ubicacion origen = Ubicacion.builder()
                .latitud(BigDecimal.valueOf(-12.04637300))
                .longitud(BigDecimal.valueOf(-77.04275400))
                .direccion("Av. Arequipa 123")
                .provincia("Lima")
                .build();

        Ubicacion destino = Ubicacion.builder()
                .latitud(BigDecimal.valueOf(-10.87654300))
                .longitud(BigDecimal.valueOf(-77.65432100))
                .direccion("Jr. Lima 234")
                .provincia("Haura")
                .build();

        Pair<Ubicacion, Ubicacion> ubicaciones = Pair.of(origen, destino);
        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(1)
                .fechaHoraUnion(LocalDateTime.now())
                .asientosOcupados(-2)
                .estado(EstadoViaje.SOLICITADO)
                .pasajero(pasajero)
                .viaje(viaje)
                .fechaHoraLLegada(LocalDateTime.now().plusDays(3))
                .costo(25.0)
                .abordo(false)
                .build();


        ViajeSolicitadoRequest request = new ViajeSolicitadoRequest(
                "2025-05-31 17:02:40.728967",
                -2,
                BigDecimal.valueOf(-12.04637300),
                BigDecimal.valueOf(-77.04275400),
                "Lima",
                "Av. Arequipa 123",
                BigDecimal.valueOf(-10.87654300),
                BigDecimal.valueOf(-77.65432100),
                "Haura",
                "Jr. Lima 234"
        );

        when(pasajeroRepository.findById(pasajeroId)).thenReturn(Optional.of(pasajero));
        when(viajeMapper.toEntity(request)).thenReturn(viaje);
        when(ubicacionMapper.OrigenDestinotoEntity(request)).thenReturn(ubicaciones);
        when(pasajeroViajeMapper.toEntity(request)).thenReturn(boleto);

        ViajeSolicitadoResponse response = new ViajeSolicitadoResponse(
                1,
                "2025-05-31 17:02:40.728967",
                "2025-06-03 17:02:40.728967",
                "Lima",
                "Haura",
                "Av. Arequipa 123",
                "Jr. Lima 234",
                25.0,
                -2,
                EstadoViaje.SOLICITADO.toString()
        );

        when(viajeMapper.toViajeSolicitadoResponse(viaje, boleto, origen, destino)).thenReturn(response);


        assertThrows(BusinessRuleException.class, () -> viajeService.crearViajeSolicitado(pasajeroId, request));
    }

    @Test
    @DisplayName("UH21 - CP01 - Visualizar historial de viajes de un pasajero con exito")
    void verHistorialViajesPasajero_success_returnsHistory() {
        Integer pasajeroId = 1;
        Pasajero pasajero = Pasajero.builder().id(pasajeroId).build();

        Viaje viaje1 = Viaje.builder().id(1).build();
        Ubicacion origen1 = Ubicacion.builder()
                .latitud(BigDecimal.valueOf(-12.04637300))
                .longitud(BigDecimal.valueOf(-77.04275400))
                .direccion("Av. Arequipa 123")
                .provincia("Lima")
                .build();
        Ubicacion destino1 = Ubicacion.builder()
                .latitud(BigDecimal.valueOf(-10.87654300))
                .longitud(BigDecimal.valueOf(-77.65432100))
                .direccion("Jr. Leon Velarde 234")
                .provincia("Haura")
                .build();
        PasajeroViaje boleto1 = PasajeroViaje.builder()
                .id(1)
                .fechaHoraUnion(LocalDateTime.parse("2025-05-31T17:02:40.728967"))
                .asientosOcupados(2)
                .estado(EstadoViaje.COMPLETADO)
                .pasajero(pasajero)
                .viaje(viaje1)
                .fechaHoraLLegada(LocalDateTime.parse("2025-06-03T17:02:40.728967"))
                .costo(25.0)
                .abordo(false)
                .build();


        Viaje viaje2 = Viaje.builder().id(2).build();
        Ubicacion origen2 = Ubicacion.builder()
                .latitud(BigDecimal.valueOf(-12.04637300))
                .longitud(BigDecimal.valueOf(-77.04275400))
                .direccion("Av. Arequipa 456")
                .provincia("Lima")
                .build();
        Ubicacion destino2 = Ubicacion.builder()
                .latitud(BigDecimal.valueOf(-10.87654300))
                .longitud(BigDecimal.valueOf(-77.65432100))
                .direccion("Jr. Leon Velarde 567")
                .provincia("Haura")
                .build();
        PasajeroViaje boleto2 = PasajeroViaje.builder()
                .id(2)
                .fechaHoraUnion(LocalDateTime.parse("2025-05-26T17:02:40.728967"))
                .asientosOcupados(1)
                .estado(EstadoViaje.CANCELADO)
                .pasajero(pasajero)
                .viaje(viaje2)
                .fechaHoraLLegada(LocalDateTime.parse("2025-05-29T17:02:40.728967"))
                .costo(15.0)
                .abordo(false)
                .build();

        List<PasajeroViajesResponse> historial = new ArrayList<>();
        historial.add(new PasajeroViajesResponse(
                viaje1.getId(),
                boleto1.getFechaHoraUnion(),
                boleto1.getEstado(),
                "Conductor Nombres",
                "Conductor Apellidos",
                boleto1.getFechaHoraUnion(),
                boleto1.getFechaHoraLLegada(),
                boleto1.getCosto()
        ));
        historial.add(new PasajeroViajesResponse(
                viaje2.getId(),
                boleto2.getFechaHoraUnion(),
                boleto2.getEstado(),
                "Conductor Nombres 2",
                "Conductor Apellidos 2",
                boleto2.getFechaHoraUnion(),
                boleto2.getFechaHoraLLegada(),
                boleto2.getCosto()
        ));

        List<Object[]> viajeData = new ArrayList<>();
        Object[] row1 = new Object[]{
                viaje1.getId(),
                new Timestamp(boleto1.getFechaHoraUnion().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()),
                boleto1.getEstado().toString(),
                "Conductor Nombres",
                "Conductor Apellidos",
                new Timestamp(boleto1.getFechaHoraUnion().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()),
                new Timestamp(boleto1.getFechaHoraLLegada().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()),
                boleto1.getCosto()
        };
        Object[] row2 = new Object[]{
                viaje2.getId(),
                new Timestamp(boleto2.getFechaHoraUnion().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()),
                boleto2.getEstado().toString(),
                "Conductor Nombres 2",
                "Conductor Apellidos 2",
                new Timestamp(boleto2.getFechaHoraUnion().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()),
                new Timestamp(boleto2.getFechaHoraLLegada().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()),
                boleto2.getCosto()
        };

        viajeData.add(row1);
        viajeData.add(row2);

        when(pasajeroRepository.findById(pasajeroId)).thenReturn(Optional.of(pasajero));
        when(viajeRepository.getViajesByPasajeroId(pasajeroId)).thenReturn(viajeData);

        assertEquals(2, viajeData.size());

        List<PasajeroViajesResponse> historialResponse = viajeService.getViajesByPasajeroId(pasajeroId);

        assertEquals(2, historialResponse.size());
        assertEquals(historial.get(0).getViaje_id(), historialResponse.get(0).getViaje_id());
        assertEquals(historial.get(1).getViaje_id(), historialResponse.get(1).getViaje_id());
        assertEquals(historial.get(0).getEstado(), historialResponse.get(0).getEstado());
        assertEquals(historial.get(1).getEstado(), historialResponse.get(1).getEstado());
        assertEquals(historial.get(0).getConductor_nombres(), historialResponse.get(0).getConductor_nombres());
        assertEquals(historial.get(1).getConductor_nombres(), historialResponse.get(1).getConductor_nombres());
        assertEquals(historial.get(0).getConductor_apellidos(), historialResponse.get(0).getConductor_apellidos());
        assertEquals(historial.get(1).getConductor_apellidos(), historialResponse.get(1).getConductor_apellidos());
        assertEquals(historial.get(0).getCosto(), historialResponse.get(0).getCosto());
        assertEquals(historial.get(1).getCosto(), historialResponse.get(1).getCosto());
    }

    @Test
    @DisplayName("UH21 - CP02 - Visualizar historial de viajes de un pasajero sin viajes")
    void verHistorialViajesPasajero_noViajes_trowsExeption() {
        Integer pasajeroId = 1;
        Pasajero pasajero = Pasajero.builder().id(pasajeroId).build();

        when(pasajeroRepository.findById(pasajeroId)).thenReturn(Optional.of(pasajero));
        when(viajeRepository.getViajesByPasajeroId(pasajeroId)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> {
            viajeService.getViajesByPasajeroId(pasajeroId);
        });
    }

}
