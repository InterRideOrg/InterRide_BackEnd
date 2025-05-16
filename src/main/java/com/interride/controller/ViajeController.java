package com.interride.controller;


import com.interride.dto.response.DetalleViajeResponse;
import com.interride.dto.response.PasajeroViajesResponse;
import com.interride.dto.response.ViajeEnCursoResponse;
import com.interride.service.ViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}