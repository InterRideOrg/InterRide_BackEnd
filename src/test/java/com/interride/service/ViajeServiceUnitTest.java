package com.interride.service;

import com.interride.dto.request.ViajeSolicitadoRequest;
import com.interride.dto.response.ViajeAceptadoResponse;
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
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
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
    @DisplayName("UH11 - CP01 - Aceptar viaje con exito")
    void aceptarViaje_success_returnsAccepted() {
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
    }

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

}
