package com.interride.controller;

import com.interride.dto.request.CreateCalificacionRequest;
import com.interride.dto.request.UpdateCalificacionRequest;
import com.interride.dto.response.CalificacionPromedioConductorResponse;
import com.interride.dto.response.CalificacionResponse;
import com.interride.model.entity.Calificacion;
import com.interride.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/calificaciones")
public class CalificacionController {
    private final CalificacionService calificacionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR', 'PASAJERO')")
    public ResponseEntity<List<CalificacionResponse>> getAll() {
        List<CalificacionResponse> calificaciones = calificacionService.getAll();
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }

    @GetMapping("/viaje/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR', 'PASAJERO')")
    public ResponseEntity<List<CalificacionResponse>> getCalificacionByViajeId(@PathVariable Integer id){
        List<CalificacionResponse> calificacionesPorViaje = calificacionService.findByViajeId(id);
        return new ResponseEntity<>(calificacionesPorViaje, HttpStatus.OK);
    }

    @GetMapping("/conductor/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR', 'PASAJERO')")
    public ResponseEntity<CalificacionPromedioConductorResponse> getCalificacionPromedioYComentariosByConductorId(@PathVariable Integer id){
        CalificacionPromedioConductorResponse calificaciones = calificacionService.findAverageRatingAndCommentsByConductorId(id);
        return ResponseEntity.ok(calificaciones);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<CalificacionResponse> createCalificacion(@Valid @RequestBody CreateCalificacionRequest calificacion){
        CalificacionResponse nuevaCalificacion = calificacionService.create(calificacion);
        return new ResponseEntity<>(nuevaCalificacion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PASAJERO')")
    public ResponseEntity<CalificacionResponse> updateCalificacion(@PathVariable Integer id,
                                               @Valid @RequestBody UpdateCalificacionRequest calificacion){
        CalificacionResponse calificacionActual = calificacionService.update(id, calificacion);
        return new ResponseEntity<>(calificacionActual, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<CalificacionResponse> deleteCalificacion(@PathVariable Integer id){
        calificacionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{pasajeroId}/{viajeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR', 'PASAJERO')")
    public ResponseEntity<CalificacionResponse> getCalificacionByPasajeroIdAndViajeId(
            @PathVariable Integer pasajeroId,
            @PathVariable Integer viajeId) {
        CalificacionResponse calificacion = calificacionService.getByPasajeroIdAndViajeId(pasajeroId, viajeId);
        return ResponseEntity.ok(calificacion);
    }
}
