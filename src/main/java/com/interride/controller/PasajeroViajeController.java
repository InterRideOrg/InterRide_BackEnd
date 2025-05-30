package com.interride.controller;

import com.interride.dto.response.BoletoCanceladoResponse;
import com.interride.service.PasajeroViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boletos")
@RequiredArgsConstructor
public class PasajeroViajeController {
    private final PasajeroViajeService pasajeroViajeService;

    @PutMapping("/{id_boleto}/cancelar")
    public ResponseEntity<BoletoCanceladoResponse> cancelarBoleto(@PathVariable Integer id_boleto) {
        BoletoCanceladoResponse response = pasajeroViajeService.ObtenerViajeCanceladoById(id_boleto);
        return ResponseEntity.ok(response);
    }

}
