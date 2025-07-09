package com.interride.service;

import com.interride.dto.request.UbicacionRequest;
import com.interride.dto.response.BoletoCanceladoResponse;
import com.interride.dto.response.BoletoCompletadoResponse;
import com.interride.dto.response.BoletoUnionResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.mapper.PasajeroViajeMapper;
import com.interride.mapper.UbicacionMapper;
import com.interride.model.entity.*;
import com.interride.model.enums.EstadoViaje;
import com.interride.repository.NotificacionRepository;
import com.interride.repository.PasajeroViajeRepository;
import com.interride.repository.UbicacionRepository;
import com.interride.repository.ViajeRepository;
import com.interride.service.Impl.PasajeroViajeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PasajeroViajeServiceUnitTest {
    @Mock private PasajeroViajeRepository pasajeroViajeRepository;
    @Mock private ViajeRepository viajeRepository;
    @Mock private UbicacionRepository ubicacionRepository;
    @Mock private NotificacionRepository notificacionRepository;
    @Mock private UbicacionMapper ubicacionMapper;
    @Mock private PasajeroViajeMapper pasajeroViajeMapper;

    @InjectMocks
    private PasajeroViajeServiceImpl pasajeroViajeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS

    @Test
    @DisplayName("CP01 - Cancelar boleto aceptado y por ende el viaje por ser el único pasajero")
    void cancelarBoleto_unicoPasajero_cancelaViaje() {
        Pasajero pasajero = Pasajero.builder().id(1).build();
        Viaje viaje = Viaje.builder()
                .id(1).estado(EstadoViaje.ACEPTADO)
                .asientosDisponibles(1)
                .asientosOcupados(1)
                .fechaHoraPartida(LocalDateTime.now())
                .conductor(Conductor.builder().id(1).build())
                .build();

        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(99).estado(EstadoViaje.ACEPTADO)
                .asientosOcupados(1)
                .pasajero(pasajero)
                .viaje(viaje)
                .costo(25.0)
                .build();

        when(pasajeroViajeRepository.findById(99)).thenReturn(Optional.of(boleto));
        when(viajeRepository.findById(1)).thenReturn(Optional.of(viaje));
        when(ubicacionRepository.findByViajeId(1)).thenReturn(Ubicacion.builder().provincia("Lima").build());
        when(ubicacionRepository.findByPasajeroViajeId(99)).thenReturn(Ubicacion.builder().provincia("Cusco").build());

        BoletoCanceladoResponse response = pasajeroViajeService.cancelarBoleto(99);

        assertNotNull(response);
        assertEquals(99, response.id());
        assertEquals("CANCELADO", response.estado());
    }

    @Test
    @DisplayName("CP02 - Cancelar boleto solicitado")
    void cancelarBoleto_solicitado_cancelaViaje() {
        Pasajero pasajero = Pasajero.builder().id(1).build();
        Viaje viaje = Viaje.builder()
                .id(1)
                .estado(EstadoViaje.ACEPTADO)
                .fechaHoraPartida(LocalDateTime.now())
                .conductor(Conductor.builder().id(1).build())
                .build();

        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(101)
                .estado(EstadoViaje.SOLICITADO)
                .pasajero(pasajero)
                .viaje(viaje)
                .costo(20.0)
                .build();

        when(pasajeroViajeRepository.findById(101)).thenReturn(Optional.of(boleto));
        when(viajeRepository.findById(1)).thenReturn(Optional.of(viaje));
        when(ubicacionRepository.findByViajeId(1)).thenReturn(Ubicacion.builder().provincia("Arequipa").build());
        when(ubicacionRepository.findByPasajeroViajeId(101)).thenReturn(Ubicacion.builder().provincia("Trujillo").build());

        BoletoCanceladoResponse response = pasajeroViajeService.cancelarBoleto(101);

        assertNotNull(response);
        assertEquals(101, response.id());
        assertEquals("CANCELADO", response.estado());
    }

    @Test
    @DisplayName("CP03 - Error al cancelar boleto con estado COMPLETADO")
    void cancelarBoleto_estadoInvalido_throwsException() {
        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(123)
                .estado(EstadoViaje.COMPLETADO)
                .viaje(Viaje.builder().id(1).build())
                .pasajero(Pasajero.builder().id(1).build())
                .build();

        when(pasajeroViajeRepository.findById(123)).thenReturn(Optional.of(boleto));
        when(viajeRepository.findById(1)).thenReturn(Optional.of(boleto.getViaje()));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> pasajeroViajeService.cancelarBoleto(123));
        assertEquals("El boleto no se encuentra en un estado válido para ser cancelado.", ex.getMessage());
    }

    @Test
    @DisplayName("CP04 - Unirse a un viaje exitosamente")
    void createBoletoUnion_success() {
        Integer pasajeroId = 1, viajeId = 1, asientosOcupados = 1;
        UbicacionRequest ubicacionRequest = new UbicacionRequest(
                new BigDecimal("10.1111"),
                new BigDecimal("20.2222"),
                "Destino Direccion",
                "Destino Provincia"
        );

        Viaje viaje = Viaje.builder()
                .id(viajeId)
                .estado(EstadoViaje.ACEPTADO)
                .asientosDisponibles(3)
                .asientosOcupados(1)
                .conductor(Conductor.builder().id(5).build())
                .build();

        Ubicacion ubicacionDestino = Ubicacion.builder()
                .direccion("Destino Direccion")
                .provincia("Destino Provincia")
                .latitud(new BigDecimal("10.1111"))
                .longitud(new BigDecimal("20.2222"))
                .build();

        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(99)
                .pasajero(Pasajero.builder().id(pasajeroId).build())
                .viaje(viaje)
                .estado(EstadoViaje.ACEPTADO)
                .fechaHoraUnion(LocalDateTime.now())
                .fechaHoraLLegada(LocalDateTime.now().plusDays(2))
                .costo(25.0)
                .asientosOcupados(asientosOcupados)
                .build();

        when(viajeRepository.findById(viajeId)).thenReturn(Optional.of(viaje));
        when(pasajeroViajeRepository.findPasajerosAceptadosByViajeId(viajeId)).thenReturn(List.of());
        when(pasajeroViajeRepository.save(any(PasajeroViaje.class))).thenReturn(boleto);
        when(ubicacionMapper.toEntity(ubicacionRequest)).thenReturn(ubicacionDestino);
        when(ubicacionRepository.save(any(Ubicacion.class))).thenReturn(ubicacionDestino);
        when(ubicacionRepository.findByViajeId(viajeId)).thenReturn(
                Ubicacion.builder()
                        .direccion("Origen Direccion")
                        .provincia("Origen Provincia")
                        .latitud(new BigDecimal("11.1111"))
                        .longitud(new BigDecimal("21.2222"))
                        .build()
        );

        BoletoUnionResponse response = pasajeroViajeService.createBoletoUnion(
                pasajeroId, viajeId, asientosOcupados, ubicacionRequest
        );

        assertNotNull(response);
        assertEquals(pasajeroId, response.pasajeroId());
        assertEquals(viajeId, response.viajeId());
        assertEquals("Destino Provincia", response.provinciaDestino());
        assertEquals("Origen Provincia", response.provinciaOrigen());
        assertEquals("Destino Direccion", response.direccionDestinO());
        assertEquals("Origen Direccion", response.direccionOrigen());
    }

    @Test
    @DisplayName("CP05 - Finalizar boleto exitosamente y completar viaje")
    void finalizarBoleto_success() {
        Pasajero pasajero = Pasajero.builder().id(1).username("Carlos").build();
        Viaje viaje = Viaje.builder().id(1).conductor(Conductor.builder().id(5).build()).build();

        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(10)
                .estado(EstadoViaje.EN_CURSO)
                .pasajero(pasajero)
                .viaje(viaje)
                .costo(40.0)
                .fechaHoraLLegada(LocalDateTime.now())
                .build();

        when(pasajeroViajeRepository.findById(10)).thenReturn(Optional.of(boleto));
        when(viajeRepository.cantidadBoletosEnCursoPorViaje(1)).thenReturn(0);
        when(pasajeroViajeMapper.toBoletoCompletadoResponse(any(), anyString()))
                .thenReturn(new BoletoCompletadoResponse(
                        boleto.getId(),
                        boleto.getFechaHoraLLegada().toString(),
                        boleto.getCosto(),
                        EstadoViaje.COMPLETADO,
                        boleto.getPasajero().getId(),
                        boleto.getViaje().getId(),
                        "Viaje de " + boleto.getPasajero().getUsername() + " finalizado. Monto recibido: S/40.00"
                ));


        BoletoCompletadoResponse response = pasajeroViajeService.finalizarBoleto(10);

        assertNotNull(response);
        assertEquals(EstadoViaje.COMPLETADO, response.estado());
        assertEquals(40.0, response.costo());
        assertEquals("Viaje de Carlos finalizado. Monto recibido: S/40.00", response.mensaje());
    }

    @Test
    @DisplayName("CP06 - Error al finalizar un boleto que no está en curso")
    void finalizarBoleto_estadoInvalido_throwsException() {
        PasajeroViaje boleto = PasajeroViaje.builder()
                .id(11)
                .estado(EstadoViaje.SOLICITADO)
                .viaje(Viaje.builder().id(2).build())
                .build();

        when(pasajeroViajeRepository.findById(11)).thenReturn(Optional.of(boleto));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> pasajeroViajeService.finalizarBoleto(11));

        assertEquals("Solo se puede finalizar un viaje en curso.", ex.getMessage());
    }

}
