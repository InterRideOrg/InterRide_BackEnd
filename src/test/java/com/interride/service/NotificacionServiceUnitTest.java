package com.interride.service;

import com.interride.dto.response.NotificacionPasajeroResponse;
import com.interride.mapper.NotificacionMapper;
import com.interride.model.entity.Notificacion;
import com.interride.model.entity.Pasajero;
import com.interride.repository.NotificacionRepository;
import com.interride.repository.PasajeroRepository;
import com.interride.service.Impl.NotificacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class NotificacionServiceUnitTest {
    @Mock private NotificacionRepository notificacionRepository;
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private NotificacionMapper notificacionMapper;

    @InjectMocks
    private NotificacionServiceImpl notificacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS

    @Test
    @DisplayName("UH14 - CP01 - Obtener notificaciones de pasajero")
    void verNotificacionesPasajero_success () {
        Integer pasajeroId = 1;

        Pasajero pasajero = Pasajero.builder()
                .id(pasajeroId)
                .nombre("Juan")
                .apellidos("Pérez")
                .build();
        Notificacion notificacion = Notificacion.builder()
                .id(1)
                .mensaje("Mensaje de prueba")
                .pasajero(pasajero)
                .leido(false)
                .fechaHoraEnvio(LocalDateTime.parse("2025-05-29T17:02:40.728967"))
                .build();
        Notificacion notificacion2 = Notificacion.builder()
                .id(2)
                .mensaje("Otro mensaje de prueba")
                .pasajero(pasajero)
                .leido(false)
                .fechaHoraEnvio(LocalDateTime.parse("2025-05-29T17:02:40.728967"))
                .build();

        when(pasajeroRepository.findById(pasajeroId)).thenReturn(Optional.of(pasajero));
        when(notificacionRepository.findByPasajeroId(pasajeroId)).thenReturn(List.of(notificacion, notificacion2));
        when(notificacionMapper.toNotificacionPasajeroResponse(notificacion)).thenReturn(
                new NotificacionPasajeroResponse(
                        notificacion.getId(),
                        notificacion.getMensaje(),
                        notificacion.getFechaHoraEnvio().toString(),
                        notificacion.getLeido(),
                        notificacion.getPasajero().getId()
                )
        );
        when(notificacionMapper.toNotificacionPasajeroResponse(notificacion2)).thenReturn(
                new NotificacionPasajeroResponse(
                        notificacion2.getId(),
                        notificacion2.getMensaje(),
                        notificacion2.getFechaHoraEnvio().toString(),
                        notificacion2.getLeido(),
                        notificacion2.getPasajero().getId()
                )
        );

        List<NotificacionPasajeroResponse> notificaciones = notificacionService.obtenerNotificacionesPasajero(pasajeroId);

        assertEquals(2, notificaciones.size());
        assertEquals(notificacion.getPasajero().getId(), notificacion2.getPasajero().getId());
        assertEquals(notificacion.getMensaje(), notificaciones.get(0).mensaje());
        assertEquals(notificacion2.getMensaje(), notificaciones.get(1).mensaje());
    }
}
