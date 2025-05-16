package com.interride.controller;

import com.interride.dto.ConductorAsignadoResponse;
import com.interride.service.ConductorAsignadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pasajero-viajes")
public class ConductorAsignadoController {

    @Autowired
    private ConductorAsignadoService conductorAsignadoService;

    @GetMapping("/{pasajeroViajeId}/conductor")
    public ResponseEntity<ConductorAsignadoResponse> obtenerPerfilConductor(
            @PathVariable Integer pasajeroViajeId,
            @RequestParam Integer pasajeroId // en el sistema deberia obtenerse el JWT/token
    ) {
        ConductorAsignadoResponse response = conductorAsignadoService.obtenerConductorAsignado(pasajeroViajeId, pasajeroId);
        return ResponseEntity.ok(response);
    }
}
