package com.interride.service;

import com.interride.dto.response.ConductorAsignadoResponse;

public interface ConductorAsignadoService {
    ConductorAsignadoResponse obtenerConductorAsignado(Integer pasajeroViajeId, Integer pasajeroId);
}