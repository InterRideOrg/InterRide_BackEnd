package com.interride.controller;

import com.interride.dto.request.UbicacionRequest;
import com.interride.dto.response.*;
import com.interride.model.enums.EstadoViaje;
import com.interride.service.PasajeroViajeService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id_pasajero}/{id_viaje}")
    @PreAuthorize("hasAnyRole('PASAJERO', 'CONDUCTOR')")
    public ResponseEntity<BoletoResponse> getBoletoByPasajeroIdAndViajeId(
            @PathVariable Integer id_pasajero,
            @PathVariable Integer id_viaje) {
        BoletoResponse response = pasajeroViajeService.getBoletoByPasajeroIdAndViajeId(id_pasajero, id_viaje);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id_pasajero}")
    @PreAuthorize("hasAnyRole('PASAJERO')")
    public ResponseEntity<List<BoletoResponse>> getBoletosByPasajeroIdAndState(
            @PathVariable Integer id_pasajero,
            @RequestParam EstadoViaje state) {
        List<BoletoResponse> response = pasajeroViajeService.getBoletosByPasajeroIdAndState(id_pasajero, state);
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

    @PutMapping("/{id_pasajero}/{id_viaje}/abordar")
    @PreAuthorize("hasAnyRole('PASAJERO')")
    public ResponseEntity<BoletoAbordoResponse> abordarViaje(
            @PathVariable Integer id_pasajero,
            @PathVariable Integer id_viaje) {
        BoletoAbordoResponse response = pasajeroViajeService.abordarViaje(id_pasajero, id_viaje);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/viaje/{viajeId}")
    @PreAuthorize("hasAnyRole('CONDUCTOR', 'PASAJERO', 'ADMIN')")
    public ResponseEntity<List<BoletoResponse>> getBoletosByViajeId(@PathVariable Integer viajeId) {
        List<BoletoResponse> boletos = pasajeroViajeService.getBoletosByViajeId(viajeId);
        return ResponseEntity.ok(boletos);
    }

}
