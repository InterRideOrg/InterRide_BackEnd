package com.interride.service;

import com.interride.dto.request.CreatePagoRequest;
import com.interride.dto.request.UpdatePagoRequest;
import com.interride.dto.response.PagoResponse;
import com.interride.model.entity.Pago;
import com.interride.model.entity.Tarjeta;

import java.util.List;

public interface PagoService {
    List<PagoResponse> getPagosByPasajeroId(Integer pasajeroId);
    List<PagoResponse> getPagosByConductorId(Integer conductorId);
    List<PagoResponse> getPagosByViajeId(Integer viajeId);
    PagoResponse createPagoEfectivo(CreatePagoRequest pago);
    PagoResponse createPagoTarjeta(CreatePagoRequest request, Integer tarjetaId);
    PagoResponse updatePago(Integer id, UpdatePagoRequest request);
}
