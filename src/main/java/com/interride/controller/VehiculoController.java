package com.interride.controller;

import com.interride.dto.VehiculoRequest;
import com.interride.dto.VehiculoResponse;
import com.interride.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {
    @Autowired
    private VehiculoService vehiculoService;

    @PostMapping("/registrar")
    public ResponseEntity<VehiculoResponse> registrarVehiculo(@RequestBody VehiculoRequest request) {
        VehiculoResponse response = vehiculoService.registrarVehiculo(request);
        return ResponseEntity.ok(response);
    }
}
