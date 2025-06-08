package com.interride.controller;

import com.interride.dto.request.ActualizarConductorPerfilRequest;
import com.interride.dto.response.ConductorPerfilActualizadoResponse;
import com.interride.service.ConductorService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RequestMapping("/conductor")
@RestController
@RequiredArgsConstructor
public class ConductorController {
    private final ConductorService conductorService;
    @PutMapping("/{id}")
    public ConductorPerfilActualizadoResponse actualizarPerfilPasajero(@PathVariable Integer id, @RequestBody ActualizarConductorPerfilRequest perfilActualizado) {
        return conductorService.actualizarPerfilConductor(id, perfilActualizado);
    }
}