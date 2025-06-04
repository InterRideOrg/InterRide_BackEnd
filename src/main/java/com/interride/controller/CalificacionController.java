package com.interride.controller;

import com.interride.dto.request.CreateCalificacionRequest;
import com.interride.dto.request.UpdateCalificacionRequest;
import com.interride.dto.response.CalificacionResponse;
import com.interride.model.entity.Calificacion;
import com.interride.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/calificaciones")
public class CalificacionController {
    private final CalificacionService calificacionService;

    @GetMapping
    public ResponseEntity<List<CalificacionResponse>> getAll() {
        List<CalificacionResponse> calificaciones = calificacionService.getAll();
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }

    @GetMapping("/viaje/{id}")
    public ResponseEntity<List<CalificacionResponse>> getCalificacionByViajeId(@PathVariable Integer id){
        List<CalificacionResponse> calificacionesPorViaje = calificacionService.findByViajeId(id);
        return new ResponseEntity<>(calificacionesPorViaje, HttpStatus.OK);
    }

    @GetMapping("/conductor/{id}")
    public ResponseEntity<List<CalificacionResponse>> getCalificacionByConductorId(@PathVariable Integer id){
        List<CalificacionResponse> calificacionesPorConductor = calificacionService.findByConductorId(id);
        return new ResponseEntity<>(calificacionesPorConductor, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CalificacionResponse> createCalificacion(@Valid @RequestBody CreateCalificacionRequest calificacion){
        CalificacionResponse nuevaCalificacion = calificacionService.create(calificacion);
        return new ResponseEntity<>(nuevaCalificacion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionResponse> updateCalificacion(@PathVariable Integer id,
                                               @Valid @RequestBody UpdateCalificacionRequest calificacion){
        CalificacionResponse calificacionActual = calificacionService.update(id, calificacion);
        return new ResponseEntity<>(calificacionActual, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CalificacionResponse> deleteCalificacion(@PathVariable Integer id){
        calificacionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
