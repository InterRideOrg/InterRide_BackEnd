package com.interride.controller;


import com.interride.dto.request.RegistroDeVehiculoRequest;
import com.interride.dto.request.VehiculoRequest;
import com.interride.dto.response.VehiculoResponse;
import com.interride.model.entity.Vehiculo;
import com.interride.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/vehiculo")
@PreAuthorize("hasAnyRole( 'CONDUCTOR')")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PutMapping("/actualizar/{conductorId}")
    public ResponseEntity<VehiculoResponse> actualizarVehiculo(
            @PathVariable Integer conductorId,
            @RequestBody @Valid VehiculoRequest request) {

        VehiculoResponse actualizado = vehiculoService.update(conductorId, request);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/registrar/{usuarioId}")
    public ResponseEntity<VehiculoResponse> registrarVehiculo(
            @PathVariable Integer usuarioId,
            @RequestBody RegistroDeVehiculoRequest registroDeVehiculoRequest) {
        VehiculoResponse registrado = vehiculoService.registrar(usuarioId, registroDeVehiculoRequest);
        return ResponseEntity.ok(registrado);
    }
}
