package com.interride.controller;



import com.interride.dto.request.ViajeSolicitadoRequest;
import com.interride.dto.response.*;
import com.interride.service.ViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class ViajeController {
    private final ViajeService viajeService;


    @GetMapping("/{id_pasajero}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<List<PasajeroViajesResponse>> getViajesCompletadosByPasajeroId(@PathVariable Integer id_pasajero) {
        List<PasajeroViajesResponse> response = viajeService.getViajesCompletadosByPasajeroId(id_pasajero);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id_pasajero}/{id_viaje}/details")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR', 'PASAJERO')")
    public ResponseEntity<DetalleViajeResponse> obtenerDetalleViaje(@PathVariable Integer id_pasajero, @PathVariable Integer id_viaje) {
        return ResponseEntity.ok(viajeService.obtenerDetalleViaje(id_viaje, id_pasajero));
    }
    @GetMapping("/{id_pasajero}/current")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR', 'PASAJERO')")
    public ResponseEntity<ViajeEnCursoResponse> obtenerDetallesViajeEnCurso(@PathVariable Integer id_pasajero) {
        ViajeEnCursoResponse response = viajeService.obtenerDetalleViajeEnCurso(id_pasajero);
        return ResponseEntity.ok(response);
    }

    // Conductor cancela viaje
    @PutMapping("/{id_viaje}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR')")
    public ResponseEntity<ViajeCanceladoResponse> cancelarViaje(@PathVariable("id_viaje") Integer idViaje) {
        ViajeCanceladoResponse response = viajeService.cancelarViaje(idViaje);
        return ResponseEntity.ok(response);
    }

    //Viajes que el pasajero busca para unirse
    @GetMapping("/viajesDisponibles")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<List<ViajeDisponibleResponse>> obtenerViajesDisponibles(){
        List<ViajeDisponibleResponse> response  = viajeService.obtenerViajesDisponibles();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/viajesCompletados/{id_conductor}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR')")
    public ResponseEntity<List<ViajeCompletadoResponse>> obtenerViajesCompletados(@PathVariable Integer id_conductor) {
        List<ViajeCompletadoResponse> response = viajeService.obtenerViajesCompletados(id_conductor);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id_viaje}/aceptar/{id_conductor}")
    @PreAuthorize("hasAnyRole('CONDUCTOR')")
    public ResponseEntity<ViajeAceptadoResponse> aceptarViaje(
            @PathVariable("id_viaje") Integer idViaje,
            @PathVariable("id_conductor") Integer idConductor
    ) {
        ViajeAceptadoResponse response = viajeService.aceptarViaje(idViaje, idConductor);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id_viaje}/empezar/{id_conductor}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR')")
    public ResponseEntity<?> empezarViaje(
            @PathVariable("id_viaje") Integer idViaje,
            @PathVariable("id_conductor") Integer idConductor) {
        boolean complete = viajeService.empezarViaje(idViaje, idConductor);
        if (complete) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo iniciar el viaje");
        }
    }

    @PostMapping("/solicitar/{id_pasajero}")
    @PreAuthorize("hasAnyRole('PASAJERO')")
    public ResponseEntity<ViajeSolicitadoResponse> solicitarViaje(@PathVariable("id_pasajero") Integer id, @Valid @RequestBody ViajeSolicitadoRequest request) {
        ViajeSolicitadoResponse response = viajeService.crearViajeSolicitado(id, request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/viajeCompletado/detalle/{viajeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR')")
    public ResponseEntity<ViajeCompletadoConductorResponse> obtenerDetalleViajeParaConductor(@PathVariable Integer viajeId) {
        ViajeCompletadoConductorResponse detalle = viajeService.verDetalleViajeCompletadoPorConductor(viajeId);
        return ResponseEntity.ok(detalle);
    }

}

