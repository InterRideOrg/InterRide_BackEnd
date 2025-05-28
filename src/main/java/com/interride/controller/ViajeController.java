package com.interride.controller;



import com.interride.dto.response.*;
import com.interride.service.ViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class ViajeController {
    private final ViajeService viajeService;


    @GetMapping("/{id_pasajero}/history")
    public ResponseEntity<List<PasajeroViajesResponse>> getViajesByPasajeroId(@PathVariable Integer id_pasajero) {
        List<PasajeroViajesResponse> response = viajeService.getViajesByPasajeroId(id_pasajero);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id_pasajero}/{id_viaje}/details")
    public ResponseEntity<DetalleViajeResponse> obtenerDetalleViaje(@PathVariable Integer id_viaje, @PathVariable Integer id_pasajero) {
        return ResponseEntity.ok(viajeService.obtenerDetalleViaje(id_viaje, id_pasajero));
    }
    @GetMapping("/{id_pasajero}/current")
    public ResponseEntity<ViajeEnCursoResponse> obtenerDetallesViajeEnCurso(@PathVariable Integer id_pasajero) {
        ViajeEnCursoResponse response = viajeService.obtenerDetalleViajeEnCurso(id_pasajero);
        return ResponseEntity.ok(response);
    }

    // Conductor cancela viaje
    @PutMapping("/{id_conductor}/{id_viaje}/cancel")
    public ResponseEntity<?> cancelarViaje(@PathVariable("id_viaje") Integer idViaje) {
        boolean exito = viajeService.cancelarViaje(idViaje);
        if (exito) {
            return ResponseEntity.ok("Viaje cancelado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viaje no encontrado.");
        }
    }


    @GetMapping("/viajesDisponibles")
    public ResponseEntity<List<ViajeDisponibleResponse>> obtenerViajesDisponibles(
        @RequestParam String provinciaOrigen,
        @RequestParam String provinciaDestino,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaViaje

    ){
        List<ViajeDisponibleResponse> response  = viajeService.obtenerViajesDisponibles(
                provinciaOrigen,
                provinciaDestino,
                fechaViaje
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/viajesCompletados/{id_conductor}")
    public ResponseEntity<List<ViajeCompletadoResponse>> obtenerViajesCompletados(@PathVariable Integer id_conductor) {
        List<ViajeCompletadoResponse> response = viajeService.obtenerViajesCompletados(id_conductor);
        return ResponseEntity.ok(response);
    }

}

