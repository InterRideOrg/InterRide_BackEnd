package com.interride.service;

import com.interride.dto.request.ActualizarUsuarioPerfilRequest;
import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.response.ConductorPerfilActualizadoResponse;
import com.interride.dto.response.ConductorPerfilPublicoResponse;
import com.interride.dto.response.ConductorRegistroResponse;

public interface ConductorService {

    ConductorPerfilPublicoResponse obtenerPerfilConductorAsignado(Integer idViaje);
}
