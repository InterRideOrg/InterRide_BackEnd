package com.interride.service;

import com.interride.dto.*;
import com.interride.model.entity.Pasajero;

public interface PasajeroService {
    PasajeroProfileDTO register(PasajeroRegistrationDTO dto);
    Pasajero getById(Integer id);   // retorna la entidad (findById o getReferenceById)
}
