package com.interride.controller;

import com.interride.dto.request.UbicacionRequest;
import com.interride.dto.response.BoletoCanceladoResponse;
import com.interride.dto.response.BoletoCompletadoResponse;
import com.interride.dto.response.BoletoUnionResponse;
import com.interride.service.PasajeroViajeService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boletos")
@RequiredArgsConstructor
public class PasajeroViajeController {
    private final PasajeroViajeService pasajeroViajeService;

    @PutMapping("/{id_boleto}/cancelar")
    @PreAuthorize("hasAnyRole('PASAJERO')")
    public ResponseEntity<BoletoCanceladoResponse> cancelarBoleto(@PathVariable Integer id_boleto) {
        BoletoCanceladoResponse response = pasajeroViajeService.cancelarBoleto(id_boleto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/union/{id_pasajero}/{id_viaje}")
    @PreAuthorize("hasAnyRole('PASAJERO')")
    public ResponseEntity<BoletoUnionResponse> unirseAViaje(
            @PathVariable Integer id_pasajero,
            @PathVariable Integer id_viaje,
            @RequestBody UbicacionRequest request,
            @RequestParam Integer asientosOcupados
            ){
        BoletoUnionResponse response = pasajeroViajeService.createBoletoUnion(id_pasajero, id_viaje, asientosOcupados, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id_boleto}/finalizar")
    @PreAuthorize("hasAnyRole('CONDUCTOR')")
    public ResponseEntity<BoletoCompletadoResponse> finalizarBoleto(@PathVariable Integer id_boleto) {
        BoletoCompletadoResponse response = pasajeroViajeService.finalizarBoleto(id_boleto);
        return ResponseEntity.ok(response);
    }

}
