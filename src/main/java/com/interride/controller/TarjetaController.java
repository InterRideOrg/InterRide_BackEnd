package com.interride.controller;

import com.interride.dto.request.TarjetaRequest;
import com.interride.dto.response.TarjetaPasajeroResponse;
import com.interride.service.TarjetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tarjetas")
public class TarjetaController {
    private final TarjetaService tarjetaService;

    @PostMapping("/pasajero/{idPasajero}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<TarjetaPasajeroResponse> createTarjetaPasajero(@PathVariable Integer idPasajero,@RequestBody @Valid TarjetaRequest request) {
        TarjetaPasajeroResponse response = tarjetaService.createTarjetaPasajero(idPasajero, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{idPasajero}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<List<TarjetaPasajeroResponse>> getTarjetasPasajero(@PathVariable Integer idPasajero) {
        List<TarjetaPasajeroResponse> response = tarjetaService.getTarjetasPasajero(idPasajero);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idTarjeta}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO', 'CONDUCTOR')")
    public ResponseEntity<Void> deleteTarjetaPasajero(@PathVariable Integer idTarjeta) {
        tarjetaService.deleteTarjetaPasajero(idTarjeta);
        return ResponseEntity.noContent().build();
    }

}
