package com.interride.service;

import com.interride.dto.request.ActualizarPasajeroPerfilRequest;
import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.dto.response.PasajeroProfileResponse;
import com.interride.model.entity.Pasajero;

public interface PasajeroService {
    PasajeroPerfilPublicoResponse obtenerPerfilPasajero(Integer idPasajero);
    PasajeroProfileResponse register(PasajeroRegistrationRequest dto);
    Pasajero getById(Integer id);   // retorna la entidad (findById o getReferenceById)
    PasajeroPerfilPublicoResponse actualizarPerfilPasajero(Integer id, ActualizarPasajeroPerfilRequest perfilActualizado);
}
