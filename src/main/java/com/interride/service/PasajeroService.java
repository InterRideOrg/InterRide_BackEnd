package com.interride.service;

import com.interride.dto.request.ActualizarPasajeroPerfilRequest;
import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.dto.response.PasajeroRegistroResponse;
import com.interride.model.entity.Pasajero;

public interface PasajeroService {
    Pasajero getById(Integer id);   // retorna la entidad (findById o getReferenceById)
}
