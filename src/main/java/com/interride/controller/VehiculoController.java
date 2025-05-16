package com.interride.controller;


import com.interride.model.entity.Vehiculo;
import com.interride.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/vehiculo")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PutMapping("/actualizar/{conductorId}")
    public ResponseEntity<Vehiculo> actualizarVehiculo(
            @PathVariable Integer conductorId,
            @RequestBody Vehiculo vehiculo) {
        Vehiculo actualizado = vehiculoService.update(conductorId, vehiculo);
        return ResponseEntity.ok(actualizado);
    }



}
