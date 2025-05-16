package com.interride.service;

import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.dto.*;
import com.interride.model.entity.Pasajero;

public interface PasajeroService {
    PasajeroPerfilPublicoResponse obtenerPerfilPasajero(Integer idPasajero);
    PasajeroProfileDTO register(PasajeroRegistrationDTO dto);
    Pasajero getById(Integer id);   // retorna la entidad (findById o getReferenceById)
}
