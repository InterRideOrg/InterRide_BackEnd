package com.interride.service;

import com.interride.dto.response.PasajeroViajesResponse;

import java.util.List;


public interface ViajeService {
    List<PasajeroViajesResponse> getViajesByPasajeroId(Integer pasajeroId);
}
