package com.interride.controller;

import com.interride.dto.request.CreatePagoRequest;
import com.interride.dto.request.UpdatePagoRequest;
import com.interride.dto.response.PagoResponse;
import com.interride.model.entity.Pago;
import com.interride.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.ResourceTransactionManager;
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
        return new ResponseEntity<>(pagosPorPasajero, org.springframework.http.HttpStatus.OK);
    }

    @PostMapping("/tarjeta/{id}")
    public ResponseEntity<PagoResponse> createPagoTarjeta(@PathVariable Integer id, @Valid @RequestBody CreatePagoRequest request){
        PagoResponse pago = pagoService.createPagoTarjeta(request, id);
        return new ResponseEntity<>(pago, org.springframework.http.HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoResponse> updatePago(@PathVariable Integer id, @RequestBody UpdatePagoRequest request){
        PagoResponse pago = pagoService.updatePago(id, request);
        return new ResponseEntity<>(pago, org.springframework.http.HttpStatus.OK);
    }
}
