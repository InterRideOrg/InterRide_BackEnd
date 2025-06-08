package com.interride.service;

import com.interride.dto.request.ActualizarConductorPerfilRequest;
import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.response.ConductorPerfilActualizadoResponse;
import com.interride.dto.response.ConductorRegistroResponse;

public interface ConductorService {
    ConductorRegistroResponse registrarConductor(ConductorRegistroRequest request);
    ConductorPerfilActualizadoResponse actualizarPerfilConductor(Integer id, ActualizarConductorPerfilRequest perfilActualizado);
}
