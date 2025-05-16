package com.interride.service;

import com.interride.dto.response.DetalleViajeResponse;
import com.interride.dto.response.PasajeroViajesResponse;
import com.interride.dto.response.ViajeEnCursoResponse;

import java.util.List;


public interface ViajeService {
    List<PasajeroViajesResponse> getViajesByPasajeroId(Integer pasajeroId);
    DetalleViajeResponse obtenerDetalleViajeNoCancelado(Integer id, Integer idPasajero);
    DetalleViajeResponse obtenerDetalleViajeCancelado(Integer id);

    DetalleViajeResponse obtenerDetalleViaje(Integer idViaje, Integer idPasajero);
    ViajeEnCursoResponse obtenerDetalleViajeEnCurso(Integer idPasajero);
}