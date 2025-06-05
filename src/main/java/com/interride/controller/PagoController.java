package com.interride.controller;

import com.interride.dto.request.CreatePagoRequest;
import com.interride.dto.request.UpdatePagoRequest;
import com.interride.dto.response.PagoResponse;
import com.interride.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pagos")
public class PagoController {
    private final PagoService pagoService;

    @GetMapping("/pasajero/{id}")
    public ResponseEntity<List<PagoResponse>> getPagosByPasajeroId(@PathVariable Integer id){
        List<PagoResponse> pagosPorPasajero = pagoService.getPagosByPasajeroId(id);
        return ResponseEntity.ok(pagosPorPasajero);
    }

    @PostMapping("/tarjeta/{id}")
    public ResponseEntity<PagoResponse> createPagoTarjeta(@PathVariable Integer id, @Valid @RequestBody CreatePagoRequest request){
        PagoResponse pago = pagoService.createPagoTarjeta(request, id);
        return ResponseEntity.ok(pago);
    }

    @PostMapping("/efectivo")
    public ResponseEntity<PagoResponse> createPagoEfectivo(@Valid @RequestBody CreatePagoRequest request){
        PagoResponse pago = pagoService.createPagoEfectivo(request);
        return ResponseEntity.ok(pago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoResponse> completarPago(@PathVariable Integer id){
        PagoResponse pago = pagoService.completarPago(id);
        return ResponseEntity.ok(pago);
    }
}
