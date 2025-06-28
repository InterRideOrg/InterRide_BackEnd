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
public class PagoController {
    private final PagoService pagoService;

    @GetMapping("/pasajero/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<List<PagoResponse>> getPagosByPasajeroId(@PathVariable Integer id){
        List<PagoResponse> pagosPorPasajero = pagoService.getPagosByPasajeroId(id);
        return ResponseEntity.ok(pagosPorPasajero);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO', 'CONDUCTOR')")
    public ResponseEntity<PagoResponse> getPagoById(@PathVariable Integer id){
        PagoResponse pago = pagoService.getPagoById(id);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/pendientes/pasajero/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<List<PagoResponse>> getPagosPendientesByPasajeroId(@PathVariable Integer id){
        List<PagoResponse> pagosPendientes = pagoService.getPagosPendientesByPasajeroId(id);
        return ResponseEntity.ok(pagosPendientes);
    }

    @PostMapping("/tarjeta/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<PagoResponse> createPagoTarjeta(@PathVariable Integer id, @Valid @RequestBody CreatePagoRequest request){
        PagoResponse pago = pagoService.createPagoTarjeta(request, id);
        return ResponseEntity.ok(pago);
    }

    @PostMapping("/efectivo")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<PagoResponse> createPagoEfectivo(@Valid @RequestBody CreatePagoRequest request){
        PagoResponse pago = pagoService.createPagoEfectivo(request);
        return ResponseEntity.ok(pago);
    }

    @PutMapping("/{id}/{tarjetaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<PagoResponse> completarPago(@PathVariable Integer id, @PathVariable Integer tarjetaId){
        PagoResponse pago = pagoService.completarPago(id, tarjetaId);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/conductor/{id}/anual-report")
    @PreAuthorize("hasAnyRole('CONDUCTOR')")
    public ResponseEntity<List<AnnualProfitReport>> getAnnualProfitReportByConductor(
            @PathVariable Integer id,
            @RequestParam Integer year) {
        List<AnnualProfitReport> report = pagoService.getAnnualProfitReportByConductor(year, id);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/conductor/{id}/monthly-report")
    @PreAuthorize("hasAnyRole('CONDUCTOR')")
    public ResponseEntity<List<MonthlyProfitReport>> getMonthlyProfitReportByConductor(
            @PathVariable Integer id,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        List<MonthlyProfitReport> report = pagoService.getMonthlyProfitReportByConductor(year, month, id);
        return ResponseEntity.ok(report);
    }
}
