package com.interride.controller;

import com.interride.dto.response.ConductorAsignadoResponse;
import com.interride.service.ConductorAsignadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conductor-asignado")
@RequiredArgsConstructor
public class ConductorAsignadoController {

    private final ConductorAsignadoService conductorAsignadoService;

    @GetMapping("/viajes/{viajeId}/pasajero/{pasajeroId}/conductor-asignado")
    public ConductorAsignadoResponse getConductorAsignado(
            @PathVariable Integer viajeId,
            @PathVariable Integer pasajeroId) {
        return conductorAsignadoService.obtenerConductorAsignado(viajeId, pasajeroId);
    }
}