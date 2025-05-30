package com.interride.controller;

import com.interride.dto.request.UbicacionRequest;
import com.interride.dto.response.BoletoCanceladoResponse;
import com.interride.dto.response.BoletoUnionResponse;
import com.interride.service.PasajeroViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/union/{id_pasajero}/{id_viaje}")
    public ResponseEntity<BoletoUnionResponse> unirseAViaje(
            @PathVariable Integer id_pasajero,
            @PathVariable Integer id_viaje,
            @RequestBody UbicacionRequest request,
            @RequestParam Integer asientosOcupados
            ){
        BoletoUnionResponse response = pasajeroViajeService.createBoletoUnion(id_pasajero, id_viaje, asientosOcupados, request);
        return ResponseEntity.ok(response);
    }

}
