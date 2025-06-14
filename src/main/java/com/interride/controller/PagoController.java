package com.interride.controller;

import com.interride.dto.request.CreatePagoRequest;
import com.interride.dto.request.UpdatePagoRequest;
import com.interride.dto.response.AnnualProfitReport;
import com.interride.dto.response.MonthlyProfitReport;
import com.interride.dto.response.PagoResponse;
import com.interride.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pagos")
@PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR', 'PASAJERO')")
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

    @GetMapping("/conductor/{id}/anual-report")
    public ResponseEntity<List<AnnualProfitReport>> getAnnualProfitReportByConductor(
            @PathVariable Integer id,
            @RequestParam Integer year) {
        List<AnnualProfitReport> report = pagoService.getAnnualProfitReportByConductor(year, id);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/conductor/{id}/monthly-report")
    public ResponseEntity<List<MonthlyProfitReport>> getMonthlyProfitReportByConductor(
            @PathVariable Integer id,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        List<MonthlyProfitReport> report = pagoService.getMonthlyProfitReportByConductor(year, month, id);
        return ResponseEntity.ok(report);
    }
}
