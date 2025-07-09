package com.interride.service;

import com.interride.dto.request.CreateCalificacionRequest;
import com.interride.dto.request.UpdateCalificacionRequest;
import com.interride.dto.response.CalificacionPromedioConductorResponse;
import com.interride.dto.response.CalificacionResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.CalificacionMapper;
import com.interride.model.entity.Calificacion;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Viaje;
import com.interride.repository.CalificacionRepository;
import com.interride.repository.ConductorRepository;
import com.interride.repository.PasajeroRepository;
import com.interride.repository.ViajeRepository;
import com.interride.service.Impl.CalificacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class CalificacionServiceUnitTest {
    @Mock private CalificacionMapper calificacionMapper;
    @Mock private CalificacionRepository calificacionRepository;
    @Mock private ConductorRepository conductorRepository;
    @Mock private ViajeRepository viajeRepository;
    @Mock private PasajeroRepository pasajeroRepository;

    @InjectMocks
    private CalificacionServiceImpl calificacionService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    //TESTS

    @Test
    @DisplayName("CP01 - Crear calificación exitosamente")
    void createCalificacion_success() {

        Calificacion calificacion  = Calificacion.builder()
                .id(10)
                .estrellas(5)
                .comentario("Buen viaje")
                .viaje(Viaje.builder().id(1).build())
                .conductor(Conductor.builder().id(2).build())
                .pasajero(Pasajero.builder().id(3).build())
                .build();

        CreateCalificacionRequest request = new CreateCalificacionRequest(
                5, "Buen viaje", 1, 2, 3
        );

        when(calificacionMapper.toEntity(request)).thenReturn(calificacion);
        when(viajeRepository.findById(1)).thenReturn(Optional.of(Viaje.builder().id(1).build()));
        when(conductorRepository.findById(2)).thenReturn(Optional.of(Conductor.builder().id(2).build()));
        when(pasajeroRepository.findById(3)).thenReturn(Optional.of(Pasajero.builder().id(3).build()));
        when(calificacionRepository.save(calificacion)).thenReturn(calificacion);

        when(calificacionMapper.toResponse(calificacion)).thenReturn(new CalificacionResponse(10, 5, "Buen viaje", 1, 2, 3));

        CalificacionResponse response = calificacionService.create(request);

        assertNotNull(response);
        assertEquals(10, response.id());
        assertEquals("Buen viaje", response.comentario());
    }

    @Test
    @DisplayName("CP02 - Fallo al crear calificación: viaje no encontrado")
    void createCalificacion_viajeNotFound_throwsException() {
        CreateCalificacionRequest request = new CreateCalificacionRequest(5, "Viaje", 99, 2, 3);
        Calificacion calificacion = Calificacion.builder()
                .estrellas(5)
                .comentario("Buen viaje")
                .viaje(Viaje.builder().id(99).build())
                .conductor(Conductor.builder().id(2).build())
                .pasajero(Pasajero.builder().id(3).build())
                .build();

        when(calificacionMapper.toEntity(request)).thenReturn(calificacion);
        when(viajeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> calificacionService.create(request));
    }

    @Test
    @DisplayName("CP03 - Fallo al crear calificación: conductor no encontrado")
    void createCalificacion_conductorNotFound_throwsException() {
        CreateCalificacionRequest request = new CreateCalificacionRequest(5, "Buen viaje", 1, 99, 3);
        Calificacion calificacion = Calificacion.builder()
                .estrellas(5)
                .comentario("Buen viaje")
                .viaje(Viaje.builder().id(1).build())
                .conductor(Conductor.builder().id(99).build())
                .pasajero(Pasajero.builder().id(3).build())
                .build();

        when(calificacionMapper.toEntity(request)).thenReturn(calificacion);
        when(viajeRepository.findById(1)).thenReturn(Optional.of(Viaje.builder().id(1).build()));
        when(conductorRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> calificacionService.create(request));
    }

    @Test
    @DisplayName("CP04 - Fallo al crear calificación: pasajero no encontrado")
    void createCalificacion_pasajeroNotFound_throwsException() {
        CreateCalificacionRequest request = new CreateCalificacionRequest(5, "Buen viaje", 1, 2, 99);
        Calificacion calificacion = Calificacion.builder()
                .estrellas(5)
                .comentario("Buen viaje")
                .viaje(Viaje.builder().id(1).build())
                .conductor(Conductor.builder().id(2).build())
                .pasajero(Pasajero.builder().id(99).build())
                .build();

        when(calificacionMapper.toEntity(request)).thenReturn(calificacion);
        when(viajeRepository.findById(1)).thenReturn(Optional.of(Viaje.builder().id(1).build()));
        when(conductorRepository.findById(2)).thenReturn(Optional.of(Conductor.builder().id(2).build()));
        when(pasajeroRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> calificacionService.create(request));
    }

    @Test
    @DisplayName("CP05 - Fallo al crear calificación: estrellas fuera de rango")
    void createCalificacion_estrellasFueraDeRango_throwsException() {
        CreateCalificacionRequest request = new CreateCalificacionRequest(6, "Buen viaje", 1, 2, 99);
        Calificacion calificacion = Calificacion.builder()
                .estrellas(6)
                .comentario("Buen viaje")
                .viaje(Viaje.builder().id(1).build())
                .conductor(Conductor.builder().id(2).build())
                .pasajero(Pasajero.builder().id(3).build())
                .build();

        when(calificacionMapper.toEntity(request)).thenReturn(calificacion);
        when(viajeRepository.findById(1)).thenReturn(Optional.of(Viaje.builder().id(1).build()));
        when(conductorRepository.findById(2)).thenReturn(Optional.of(Conductor.builder().id(2).build()));
        when(pasajeroRepository.findById(3)).thenReturn(Optional.of(Pasajero.builder().id(3).build()));

        assertThrows(BusinessRuleException.class, () -> calificacionService.create(request));
    }


    @Test
    @DisplayName("CP06 - Obtener todas las calificaciones existentes")
    void getAllCalificaciones_success() {
        Calificacion calificacion = Calificacion.builder()
                .id(1).estrellas(5).comentario("Excelente")
                .viaje(Viaje.builder().id(1).build())
                .conductor(Conductor.builder().id(1).build())
                .pasajero(Pasajero.builder().id(1).build())
                .build();

        when(calificacionRepository.findAll()).thenReturn(List.of(calificacion));
        when(calificacionMapper.toResponse(calificacion))
                .thenReturn(new CalificacionResponse(1, 5, "Excelente", 1, 1, 1));

        List<CalificacionResponse> result = calificacionService.getAll();

        assertEquals(1, result.size());
        assertEquals("Excelente", result.getFirst().comentario());
    }

    @Test
    @DisplayName("CP07 - Obtener calificaciones por ID de viaje")
    void findByViajeId_success() {
        List<Calificacion> mockList = List.of(
                Calificacion.builder()
                        .id(1)
                        .estrellas(4)
                        .comentario("Viaje OK")
                        .viaje(Viaje.builder().id(99).build())
                        .conductor(Conductor.builder().id(1).build())
                        .pasajero(Pasajero.builder().id(1).build())
                        .build()
        );

        when(calificacionRepository.findByViajeId(99)).thenReturn(mockList);
        when(calificacionMapper.toResponse(any())).thenReturn(new CalificacionResponse(1, 4, "Viaje OK", 99, 1, 1));

        List<CalificacionResponse> result = calificacionService.findByViajeId(99);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Viaje OK", result.getFirst().comentario());
    }

    @Test
    @DisplayName("CP08 - Obtener promedio y comentarios de calificaciones por conductor")
    void findAverageRating_success() {
        when(calificacionRepository.findAverageRatingByConductorId(1)).thenReturn(4.5);
        when(calificacionRepository.findByConductorId(1)).thenReturn(
                List.of(Calificacion.builder()
                        .id(1)
                        .estrellas(5)
                        .comentario("Muy bien")
                        .viaje(Viaje.builder().id(1).build())
                        .conductor(Conductor.builder().id(1).build())
                        .pasajero(Pasajero.builder().id(1).build())
                        .build())
        );
        when(calificacionMapper.toResponse(any())).thenReturn(
                new CalificacionResponse(1, 5, "Muy bien", 1, 1, 1)
        );

        CalificacionPromedioConductorResponse response = calificacionService.findAverageRatingAndCommentsByConductorId(1);

        assertNotNull(response);
        assertEquals(4.5, response.calificacionPromedio());
        assertEquals(1, response.calificaciones().size());
    }

    @Test
    @DisplayName("CP09 - Fallo al obtener calificaciones: conductor sin calificaciones")
    void findAverageRating_conductorSinCalificaciones_throwsException() {
        when(calificacionRepository.findAverageRatingByConductorId(999)).thenReturn(null);
        when(calificacionRepository.findByConductorId(999)).thenReturn(List.of());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> calificacionService.findAverageRatingAndCommentsByConductorId(999));

        assertEquals("No se encontraron calificaciones para el conductor con id: 999", ex.getMessage());
    }

    @Test
    @DisplayName("CP10 - Actualización exitosa de calificación")
    void updateCalificacion_success() {
        Integer calificacionId = 1;

        Calificacion existente = Calificacion.builder()
                .id(calificacionId)
                .estrellas(3)
                .comentario("Regular")
                .viaje(Viaje.builder().id(1).build())
                .conductor(Conductor.builder().id(2).build())
                .pasajero(Pasajero.builder().id(3).build())
                .build();

        UpdateCalificacionRequest request = new UpdateCalificacionRequest(5, "Mejor comentario");

        when(calificacionRepository.findById(calificacionId)).thenReturn(Optional.of(existente));
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(existente);
        when(calificacionMapper.toResponse(existente)).thenReturn(
                new CalificacionResponse(1, 5, "Mejor comentario", 1, 2, 3)
        );

        CalificacionResponse response = calificacionService.update(calificacionId, request);

        assertNotNull(response);
        assertEquals(5, response.estrellas());
        assertEquals("Mejor comentario", response.comentario());
    }

    @Test
    @DisplayName("CP11 - Fallo al actualizar: estrellas fuera de rango")
    void updateCalificacion_estrellasFueraDeRango_throwsException() {
        UpdateCalificacionRequest request = new UpdateCalificacionRequest(0, "Comentario inválido");

        assertThrows(RuntimeException.class,
                () -> calificacionService.update(1, request),
                "El número de estrellas debe estar entre 1 y 5");
    }

    @Test
    @DisplayName("CP12 - Fallo al actualizar: calificación no encontrada")
    void updateCalificacion_calificacionNoEncontrada_throwsException() {
        Integer calificacionId = 999;
        UpdateCalificacionRequest request = new UpdateCalificacionRequest(4, "Comentario");

        when(calificacionRepository.findById(calificacionId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> calificacionService.update(calificacionId, request));

        assertEquals("Calificacion con id 999 no encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("CP13 - Eliminación exitosa de calificación")
    void deleteCalificacion_success() {
        Integer calificacionId = 1;

        Calificacion existente = Calificacion.builder()
                .id(calificacionId)
                .build();

        when(calificacionRepository.findById(calificacionId)).thenReturn(Optional.of(existente));
        doNothing().when(calificacionRepository).delete(existente);

        assertDoesNotThrow(() -> calificacionService.delete(calificacionId));
    }

    @Test
    @DisplayName("CP14 - Fallo al eliminar: calificación no encontrada")
    void deleteCalificacion_noEncontrado_throwsException() {
        Integer calificacionId = 999;
        when(calificacionRepository.findById(calificacionId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> calificacionService.delete(calificacionId));

        assertEquals("Calificacion con id 999 no encontrado", ex.getMessage());
    }

}
