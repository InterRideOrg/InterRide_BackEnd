package com.interride.service;

import com.interride.dto.request.CreatePagoRequest;
import com.interride.dto.request.UpdatePagoRequest;
import com.interride.dto.response.AnnualProfitReport;
import com.interride.dto.response.MonthlyProfitReport;
import com.interride.dto.response.PagoResponse;

import java.util.List;

public interface PagoService {
    PagoResponse getPagoById(Integer id);

    List<PagoResponse> getPagosByPasajeroId(Integer pasajeroId);
    //List<PagoResponse> getPagosByConductorId(Integer conductorId);
    //List<PagoResponse> getPagosByViajeId(Integer viajeId);

    List<PagoResponse> getPagosPendientesByPasajeroId(Integer pasajeroId);
    List<PagoResponse> getPagosCompletadosByPasajeroId(Integer pasajeroId);
    PagoResponse createPagoEfectivo(CreatePagoRequest pago);
    PagoResponse createPagoTarjeta(CreatePagoRequest request, Integer tarjetaId);
    PagoResponse completarPago(Integer id, Integer tarjetaId);

    List<AnnualProfitReport> getAnnualProfitReportByConductor(Integer year, Integer conductorId);
    List<MonthlyProfitReport> getMonthlyProfitReportByConductor(Integer year, Integer month, Integer conductorId);

}
