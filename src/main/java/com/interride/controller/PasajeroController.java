package com.interride.controller;

import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.service.PasajeroService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pasajero")
public class PasajeroController {

    private final PasajeroService pasajeroService;

    public PasajeroController(PasajeroService pasajeroService) {
        this.pasajeroService = pasajeroService;
    }

    @GetMapping("/{id}")
    public PasajeroPerfilPublicoResponse obtenerPerfilPasajero(@PathVariable Integer id) {
        return pasajeroService.obtenerPerfilPasajero(id);
    }
}