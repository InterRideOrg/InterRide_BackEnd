package com.interride.service;

import com.interride.dto.request.ViajeSolicitadoRequest;
import com.interride.dto.response.PasajeroViajesResponse;
import com.interride.dto.response.ViajeAceptadoResponse;
import com.interride.dto.response.DetalleViajeResponse;
import com.interride.dto.response.ViajeEnCursoResponse;
import com.interride.dto.response.ViajeSolicitadoResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.PasajeroViajeMapper;
import com.interride.mapper.UbicacionMapper;
import com.interride.mapper.ViajeMapper;
import com.interride.model.entity.*;
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
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.junit.jupiter.api.Assertions.*;
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

    //@Test
    //@Displayname("UH11 - CP01 - Aceptar viaje con exito")
    /*void aceptarViaje_success_returnsAccepted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        Integer idViaje = 1;
        Integer idConductor = 1;

        Viaje viaje = new Viaje();
        viaje.setId(idViaje);
        viaje.setEstado(EstadoViaje.SOLICITADO);
        viaje.setAsientosOcupados(2);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setCantidadAsientos(4);

        Conductor conductor = new Conductor();
        conductor.setId(idConductor);
        conductor.setNombre("Juan Pérez");
        conductor.setVehiculo(vehiculo);

        Pasajero pasajero = new Pasajero();
        pasajero.setId(1);

        PasajeroViaje boletoInicial = new PasajeroViaje();
        boletoInicial.setId(1);
        boletoInicial.setPasajero(pasajero);
        boletoInicial.setEstado(EstadoViaje.SOLICITADO);

        Ubicacion origen = new Ubicacion();
        origen.setProvincia("Lima");

        Ubicacion destino = new Ubicacion();
        destino.setProvincia("Haura");

        ViajeAceptadoResponse expectedResponse = new ViajeAceptadoResponse(
                idViaje,
                idConductor,
                EstadoViaje.ACEPTADO,
                "Lima",
                "Haura",
                "Av. Arequipa 123",
                LocalDateTime.parse("2025-05-31 17:02:40.728967", formatter),
                2
        );

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.of(conductor));
        when(pasajeroViajeRepository.findBoletoInicialIdByViajeId(idViaje)).thenReturn(boletoInicial);
        when(ubicacionRepository.findByViajeId(idViaje)).thenReturn(origen);
        when(ubicacionRepository.findByPasajeroViajeId(boletoInicial.getId())).thenReturn(destino);
        when(pasajeroViajeRepository.save(any(PasajeroViaje.class))).thenReturn(boletoInicial);
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(new Notificacion());
        when(viajeRepository.save(any(Viaje.class))).thenReturn(viaje);
        when(viajeMapper.toViajeAceptadoResponse(viaje, conductor, origen, destino)).thenReturn(expectedResponse);


        ViajeAceptadoResponse result = viajeService.aceptarViaje(idViaje, idConductor);

        assertEquals(idViaje, result.idViaje());
        assertEquals(EstadoViaje.ACEPTADO, result.estadoViaje());
        assertEquals("Lima", result.provinciaOrigen());
        assertEquals("Haura", result.provinciaDestino());
        assertEquals("Av. Arequipa 123", result.direccionOrigen());
        assertEquals(2, result.asientosDisponibles());
        assertEquals(EstadoViaje.ACEPTADO, boletoInicial.getEstado());
    }*/

    @Test
    @DisplayName("UH11 - CP02 - Aceptar viaje con viaje no encontrado")
    void aceptarViaje_viajeNotFound_throwsException() {
        Integer idViaje = 1;
        Integer idConductor = 1;

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> viajeService.aceptarViaje(idViaje, idConductor));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("UH11 - CP03 - Aceptar viaje con conductor no encontrado")
    void aceptarViaje_conductorNotFound_throwsException() {
        Integer idViaje = 1;
        Integer idConductor = 1;

        Viaje viaje = new Viaje();
        viaje.setId(idViaje);
        viaje.setEstado(EstadoViaje.SOLICITADO);

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> viajeService.aceptarViaje(idViaje, idConductor));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("UH11 - CP04 - Aceptar viaje con viaje no solicitado")
    void aceptarViaje_viajeNotSolicitado_throwsException() {
        Integer idViaje = 1;
        Integer idConductor = 1;

        Viaje viaje = new Viaje();
        viaje.setId(idViaje);
        viaje.setEstado(EstadoViaje.CANCELADO); // Estado no solicitado

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.of(new Conductor()));
        when(pasajeroViajeRepository.findBoletoInicialIdByViajeId(idViaje)).thenReturn(new PasajeroViaje());
        when(ubicacionRepository.findByViajeId(idViaje)).thenReturn(new Ubicacion());
        when(ubicacionRepository.findByPasajeroViajeId(any(Integer.class))).thenReturn(new Ubicacion());

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> viajeService.aceptarViaje(idViaje, idConductor));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("UH11 - CP05 - Aceptar viaje con Conductor sin vehiculo")
    void aceptarViaje_conductorSinVehiculo_throwsException() {
        Integer idViaje = 1;
        Integer idConductor = 1;

        Viaje viaje = new Viaje();
        viaje.setId(idViaje);
        viaje.setEstado(EstadoViaje.SOLICITADO);

        Conductor conductor = new Conductor();
        conductor.setId(idConductor);
        conductor.setVehiculo(null); // Sin vehículo

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.of(conductor));
        when(pasajeroViajeRepository.findBoletoInicialIdByViajeId(idViaje)).thenReturn(new PasajeroViaje());
        when(ubicacionRepository.findByViajeId(idViaje)).thenReturn(new Ubicacion());
        when(ubicacionRepository.findByPasajeroViajeId(any(Integer.class))).thenReturn(new Ubicacion());

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> viajeService.aceptarViaje(idViaje, idConductor));
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("UH11 - CP06 - Aceptar viaje con conductor sin asientos disponibles")
    void aceptarViaje_conductorSinAsientosDisponibles_throwsException() {
        Integer idViaje = 1;
        Integer idConductor = 1;

        Viaje viaje = new Viaje();
        viaje.setId(idViaje);
        viaje.setEstado(EstadoViaje.SOLICITADO);
        viaje.setAsientosOcupados(4);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setCantidadAsientos(2);

        Conductor conductor = new Conductor();
        conductor.setId(idConductor);
        conductor.setVehiculo(vehiculo);

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.of(conductor));
        when(pasajeroViajeRepository.findBoletoInicialIdByViajeId(idViaje)).thenReturn(new PasajeroViaje());
        when(ubicacionRepository.findByViajeId(idViaje)).thenReturn(new Ubicacion());
        when(ubicacionRepository.findByPasajeroViajeId(any(Integer.class))).thenReturn(new Ubicacion());

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> viajeService.aceptarViaje(idViaje, idConductor));
        System.out.println(exception.getMessage());
    }

    //@Test
    //@DisplayName("UH13 - CP01 - Notificar viaje aceptado con exito")
    /*void notificarViajeAceptado_success() {
        Integer idViaje = 1;
        Integer idConductor = 1;

        Viaje viaje = new Viaje();
        viaje.setId(idViaje);
        viaje.setEstado(EstadoViaje.SOLICITADO);
        viaje.setAsientosOcupados(2);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setCantidadAsientos(4);

        Conductor conductor = new Conductor();
        conductor.setId(idConductor);
        conductor.setNombre("Juan Pérez");
        conductor.setVehiculo(vehiculo);

        Pasajero pasajero = new Pasajero();
        pasajero.setId(2);
        pasajero.setNombre("Ana Gómez");

        PasajeroViaje boletoInicial = new PasajeroViaje();
        boletoInicial.setAsientosOcupados(2);
        boletoInicial.setPasajero(pasajero);
        boletoInicial.setEstado(EstadoViaje.SOLICITADO);



        Ubicacion origen = new Ubicacion();
        origen.setProvincia("Lima");

        Ubicacion destino = new Ubicacion();
        destino.setProvincia("Haura");

        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje("Tu viaje de " + origen.getProvincia() + " a " + destino.getProvincia() + " ha sido aceptado por el conductor " + conductor.getNombre() + ".");

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.of(conductor));
        when(pasajeroViajeRepository.findBoletoInicialIdByViajeId(idViaje)).thenReturn(boletoInicial);
        when(ubicacionRepository.findByViajeId(idViaje)).thenReturn(origen);
        when(ubicacionRepository.findByPasajeroViajeId(boletoInicial.getId())).thenReturn(destino);
        when(pasajeroViajeRepository.save(any(PasajeroViaje.class))).thenReturn(boletoInicial);
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);

        viajeService.aceptarViaje(idViaje, idConductor);

        assertEquals(EstadoViaje.ACEPTADO, boletoInicial.getEstado());
        assertEquals("Tu viaje de " + origen.getProvincia() + " a " + destino.getProvincia() + " ha sido aceptado por el conductor " + conductor.getNombre() + ".", notificacion.getMensaje());
    }*/


    @Test
    @DisplayName("UH22 - CP01 - Ver detalles de viaje terminado exitosamente")
    void verDetallesViaje_success_returnsDetails() {
        Integer viajeId = 1;
        Integer pasajeroId = 1;
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
        Conductor conductor = Conductor.builder()
                .id(1)
                .nombre("Carlos Gomez")
                .build();
        Viaje viaje = Viaje.builder()
                .id(viajeId)
                .fechaHoraPartida(LocalDateTime.now())
                .conductor(conductor)
                .estado(EstadoViaje.COMPLETADO)
                .build();
        Pasajero pasajero = Pasajero.builder()
                .id(pasajeroId)
                .nombre("Juan Perez")
                .build();
        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(1)
                .fechaHoraUnion(LocalDateTime.parse("2025-05-26T17:02:40.728967"))
                .asientosOcupados(2)
                .estado(EstadoViaje.COMPLETADO)
                .pasajero(pasajero)
                .viaje(viaje)
                .fechaHoraLLegada(LocalDateTime.parse("2025-05-26T20:02:40.728967"))
                .costo(50.0)
                .abordo(false)
                .build();
        Calificacion calificacion = Calificacion.builder()
                .id(1)
                .viaje(viaje)
                .pasajero(pasajero)
                .estrellas(3)
                .build();

        List<Object[]> viajeData = new ArrayList<>();
        Object[] data = new Object[]{
                new Timestamp(boleto.getFechaHoraUnion().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()),
                origen.getDireccion(),
                destino.getDireccion(),
                conductor.getNombre(),
                "Tarjeta",
                boleto.getCosto(),
                calificacion.getEstrellas(),
                boleto.getEstado().toString(),
        };
        viajeData.add(data);

        when(viajeRepository.findById(viajeId)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(viaje.getConductor().getId())).thenReturn(Optional.of(conductor));
        when(viajeRepository.getDetalleViajeById(viajeId, pasajeroId))
                .thenReturn(viajeData);
        DetalleViajeResponse response = viajeService.obtenerDetalleViaje(viajeId,pasajeroId);

        assertEquals(boleto.getEstado(), response.getEstado());
        assertEquals(conductor.getNombre(), response.getConductorNombreCompleto());
        assertEquals(boleto.getCosto(), response.getMontoPagado());
        assertEquals(calificacion.getEstrellas(), response.getCalificacionEstrellas());
        assertEquals(origen.getDireccion(), response.getOrigen());
        assertEquals(destino.getDireccion(), response.getDestino());
    }

    @Test
    @DisplayName("UH22 - CP01 - Ver detalles de viaje cancelado")
    void verDetallesViaje_success_returnViajeCancelado(){
        Integer viajeId = 1;
        Integer pasajeroId = 1;

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
        Conductor conductor = Conductor.builder()
                .id(1)
                .nombre("Carlos Gomez")
                .build();
        Viaje viaje = Viaje.builder()
                .id(viajeId)
                .fechaHoraPartida(LocalDateTime.now())
                .conductor(conductor)
                .estado(EstadoViaje.CANCELADO)
                .build();
        Pasajero pasajero = Pasajero.builder()
                .id(pasajeroId)
                .nombre("Juan Perez")
                .build();
        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(1)
                .fechaHoraUnion(LocalDateTime.parse("2025-05-26T17:02:40.728967"))
                .asientosOcupados(2)
                .estado(EstadoViaje.CANCELADO)
                .pasajero(pasajero)
                .viaje(viaje)
                .costo(0.0)
                .abordo(false)
                .build();

        List<Object[]> viajeData = new ArrayList<>();
        Object[] data = new Object[]{
                new Timestamp(boleto.getFechaHoraUnion().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()),
                origen.getDireccion(),
                destino.getDireccion(),
                conductor.getNombre(),
                "", // Modo de pago no disponible
                0.0, // Monto pagado no disponible
                null, // Calificación no disponible
                boleto.getEstado().toString(),
        };
        viajeData.add(data);

        when(viajeRepository.findById(viajeId)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(viaje.getConductor().getId())).thenReturn(Optional.of(conductor));
        when(viajeRepository.getDetalleViajeById(viajeId, pasajeroId))
                .thenReturn(viajeData);

        DetalleViajeResponse response = viajeService.obtenerDetalleViaje(viajeId, pasajeroId);
        assertEquals(boleto.getEstado(), response.getEstado());
        assertEquals(conductor.getNombre(), response.getConductorNombreCompleto());
        assertEquals(0.0, response.getMontoPagado());
        assertNull(response.getCalificacionEstrellas());
        assertEquals(origen.getDireccion(), response.getOrigen());
        assertEquals(destino.getDireccion(), response.getDestino());
    }

    @Test
    @DisplayName("UH25 - CP01 - Visualizar viaje en curso con exito")
    void verViajeEnCurso_success() {
        Integer idPasajero = 1;
        Integer idViaje = 1;
        // Datos de prueba
        Viaje viaje = Viaje.builder()
                .id(idViaje)
                .estado(EstadoViaje.EN_CURSO)
                .asientosOcupados(2)
                .fechaHoraPartida(LocalDateTime.parse("2025-05-31T17:02:40.728967"))
                .build();
        Conductor conductor = Conductor.builder()
                .id(1)
                .nombre("Juan Pérez Gómez")
                .apellidos("Gómez")
                .build();
        Vehiculo vehiculo = Vehiculo.builder()
                .modelo("Toyota Corolla")
                .placa("ABC-123")
                .marca("Toyota")
                .cantidadAsientos(4)
                .build();
        Ubicacion origen = Ubicacion.builder()
                .longitud(BigDecimal.valueOf(-77.04275400))
                .latitud(BigDecimal.valueOf(-12.04637300))
                .provincia("Lima")
                .build();
        Ubicacion destino = Ubicacion.builder()
                .longitud(BigDecimal.valueOf(-77.65432100))
                .latitud(BigDecimal.valueOf(-10.87654300))
                .provincia("Haura")
                .build();

        // Simulación de la respuesta del repositorio

        List<Object[]> responseData = new ArrayList<>();
        Object[] data = new Object[]{
                viaje.getId(),
                conductor.getNombre(),
                conductor.getApellidos(),
                vehiculo.getModelo(),
                vehiculo.getPlaca(),
                vehiculo.getMarca(),
                vehiculo.getCantidadAsientos(),
                viaje.getAsientosOcupados(),
                origen.getLongitud(),
                origen.getLatitud(),
                origen.getProvincia(),
                destino.getLongitud(),
                destino.getLatitud(),
                destino.getProvincia(),
                viaje.getEstado().toString(),
                new Timestamp(viaje.getFechaHoraPartida().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()),
        };
        responseData.add(data);

        when(viajeRepository.getViajeEnCursoById(idPasajero)).thenReturn(responseData);

        ViajeEnCursoResponse response = viajeService.obtenerDetalleViajeEnCurso(idPasajero);

        assertEquals(idViaje, response.getId());
        assertEquals("Lima", response.getOrigenProvincia());
        assertEquals("Haura", response.getDestinoProvincia());
        assertEquals("Juan Pérez Gómez", response.getNombreConductor());
        assertEquals(EstadoViaje.EN_CURSO, response.getEstadoViaje());
        assertEquals("Toyota Corolla", response.getModeloVehiculo());
        assertEquals("ABC-123", response.getPlacaVehiculo());
        assertEquals("Toyota", response.getMarcaVehiculo());
    }

    @Test
    @DisplayName("HU29 - CP01 - Conductor inicia viaje con exito")
    void iniciarViaje_success() {
        Integer idViaje = 1;
        Integer idConductor = 1;
        Pasajero pasajero = Pasajero.builder()
                .id(1)
                .nombre("Ana Gómez")
                .build();
        Viaje viaje = Viaje.builder()
                .id(idViaje)
                .estado(EstadoViaje.ACEPTADO)
                .asientosOcupados(2)
                .fechaHoraPartida(LocalDateTime.now())
                .build();
        Conductor conductor = Conductor.builder()
                .id(idConductor)
                .nombre("Juan Pérez Gómez")
                .apellidos("Gómez")
                .build();
        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(1)
                .pasajero(pasajero)
                .fechaHoraUnion(LocalDateTime.now())
                .asientosOcupados(2)
                .estado(EstadoViaje.ACEPTADO)
                .pasajero(pasajero)
                .viaje(viaje)
                .fechaHoraLLegada(LocalDateTime.now().plusHours(3))
                .costo(50.0)
                .abordo(true)
                .build();

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.of(conductor));
        when(pasajeroViajeRepository.findBoletoInicialIdByViajeId(idViaje)).thenReturn(boleto);
        when(pasajeroViajeRepository.save(any(PasajeroViaje.class))).thenReturn(boleto);
        when(pasajeroViajeRepository.findPasajerosAceptadosByViajeId(idViaje)).thenReturn(List.of(boleto));
        when(viajeRepository.save(any(Viaje.class))).thenReturn(viaje);
        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.of(conductor));

        viajeService.empezarViaje(idViaje, idConductor);

        assertEquals(EstadoViaje.EN_CURSO, viaje.getEstado());
    }

    @Test
    @DisplayName("HU29 - CP02 - Conductor falla iniciar el viaje por pasajero no abordo")
    void iniciarViaje_trowMissingPasajeroAbordo() {
        Integer idViaje = 1;
        Integer idConductor = 1;
        Pasajero pasajero = Pasajero.builder()
                .id(1)
                .nombre("Ana Gómez")
                .build();
        Viaje viaje = Viaje.builder()
                .id(idViaje)
                .estado(EstadoViaje.ACEPTADO)
                .asientosOcupados(2)
                .fechaHoraPartida(LocalDateTime.now())
                .build();
        Conductor conductor = Conductor.builder()
                .id(idConductor)
                .nombre("Juan Pérez Gómez")
                .apellidos("Gómez")
                .build();
        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(1)
                .pasajero(pasajero)
                .fechaHoraUnion(LocalDateTime.now())
                .asientosOcupados(2)
                .estado(EstadoViaje.ACEPTADO)
                .pasajero(pasajero)
                .viaje(viaje)
                .fechaHoraLLegada(LocalDateTime.now().plusHours(3))
                .costo(50.0)
                .abordo(false)
                .build();

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.of(conductor));
        when(pasajeroViajeRepository.findBoletoInicialIdByViajeId(idViaje)).thenReturn(boleto);
        when(pasajeroViajeRepository.save(any(PasajeroViaje.class))).thenReturn(boleto);
        when(pasajeroViajeRepository.findPasajerosAceptadosByViajeId(idViaje)).thenReturn(List.of(boleto));
        when(viajeRepository.save(any(Viaje.class))).thenReturn(viaje);
        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.of(conductor));

        //BusinessRuleException
        assertThrows(BusinessRuleException.class, () -> {
            viajeService.empezarViaje(idViaje, idConductor);
        });
    }

    @Test
    @DisplayName("HU29 - CP03 - Conductor falla iniciar el viaje por hora de partida no valida")
    void iniciarViaje_trowIncorrectHour() {
        Integer idViaje = 1;
        Integer idConductor = 1;
        Pasajero pasajero = Pasajero.builder()
                .id(1)
                .nombre("Ana Gómez")
                .build();
        Viaje viaje = Viaje.builder()
                .id(idViaje)
                .estado(EstadoViaje.ACEPTADO)
                .asientosOcupados(2)
                .fechaHoraPartida(LocalDateTime.parse("2025-05-31T17:02:40.728967"))
                .build();
        Conductor conductor = Conductor.builder()
                .id(idConductor)
                .nombre("Juan Pérez Gómez")
                .apellidos("Gómez")
                .build();
        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(1)
                .pasajero(pasajero)
                .fechaHoraUnion(LocalDateTime.now())
                .asientosOcupados(2)
                .estado(EstadoViaje.ACEPTADO)
                .pasajero(pasajero)
                .viaje(viaje)
                .fechaHoraLLegada(LocalDateTime.now().plusHours(3))
                .costo(50.0)
                .abordo(false)
                .build();

        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.of(conductor));
        when(pasajeroViajeRepository.findBoletoInicialIdByViajeId(idViaje)).thenReturn(boleto);
        when(pasajeroViajeRepository.save(any(PasajeroViaje.class))).thenReturn(boleto);
        when(pasajeroViajeRepository.findPasajerosAceptadosByViajeId(idViaje)).thenReturn(List.of(boleto));
        when(viajeRepository.save(any(Viaje.class))).thenReturn(viaje);
        when(viajeRepository.findById(idViaje)).thenReturn(Optional.of(viaje));
        when(conductorRepository.findById(idConductor)).thenReturn(Optional.of(conductor));

        //BusinessRuleException
        assertThrows(BusinessRuleException.class, () -> {
            viajeService.empezarViaje(idViaje, idConductor);
        });
    }

}
