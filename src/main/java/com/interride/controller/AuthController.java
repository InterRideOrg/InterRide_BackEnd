package com.interride.controller;

import com.interride.model.entity.Pasajero;
import com.interride.service.PasajeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final PasajeroService pasajeroService;

    // Endpoint para registrar pasajeros
    @PostMapping
    public ResponseEntity<Pasajero> register(@RequestBody Pasajero pasajero){
        Pasajero newPasajero = pasajeroService.registerPasajero(pasajero);
        return new ResponseEntity<>(newPasajero, HttpStatus.CREATED);
    }


}
