package com.interride.controller;

import com.interride.model.entity.Calificacion;
import com.interride.service.CalificacionService;
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
    public ResponseEntity<List<Calificacion>> getAll() {
        List<Calificacion> calificaciones = calificacionService.getAll();
        return new ResponseEntity<List<Calificacion>>(calificaciones, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Calificacion> getCalificacionById(@PathVariable("id") Integer id){
        Calificacion calificacion = calificacionService.findById(id);
        return new ResponseEntity<Calificacion>(calificacion, HttpStatus.OK);
    }

    @GetMapping("/viaje/{id}")
    public ResponseEntity<List<Calificacion>> getCalificacionByViajeId(@PathVariable Integer id){
        List<Calificacion> calificaciones_por_viaje = calificacionService.findByViajeId(id);
        return new ResponseEntity<List<Calificacion>>(calificaciones_por_viaje, HttpStatus.OK);
    }

    @GetMapping("/conductor/{id}")
    public ResponseEntity<List<Calificacion>> getCalificacionByConductorId(@PathVariable Integer id){
        List<Calificacion> calificaciones_por_conductor = calificacionService.findByConductorId(id);
        return new ResponseEntity<List<Calificacion>>(calificaciones_por_conductor, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Calificacion> createCalificacion(@RequestBody Calificacion calificacion){
        Calificacion nuevaCalificacion = calificacionService.create(calificacion);
        return new ResponseEntity<Calificacion>(nuevaCalificacion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> updateCalificacion(@PathVariable("id") Integer id,
                                               @RequestBody Calificacion calificacion){
        Calificacion calificacionActual = calificacionService.update(id, calificacion);
        return new ResponseEntity<Calificacion>(calificacionActual, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Calificacion> deleteCalificacion(@PathVariable("id") Integer id){
        calificacionService.delete(id);
        return new ResponseEntity<Calificacion>(HttpStatus.NO_CONTENT);
    }
}
