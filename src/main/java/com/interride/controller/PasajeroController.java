package com.interride.controller;

import com.interride.dto.request.ActualizarPasajeroPerfilRequest;
import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.service.PasajeroService;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public PasajeroPerfilPublicoResponse actualizarPerfilPasajero(@PathVariable Integer id, @RequestBody ActualizarPasajeroPerfilRequest perfilActualizado) {
        return pasajeroService.actualizarPerfilPasajero(id, perfilActualizado);
    }

}