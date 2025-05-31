package com.interride.service;

import com.interride.dto.request.RegistroDeVehiculoRequest;
import com.interride.model.entity.Vehiculo;

public interface VehiculoService {
    Vehiculo update(Integer conductorId, Vehiculo vehiculo);
    Vehiculo registrar(Integer conductorId, RegistroDeVehiculoRequest registroDeVehiculoRequest);
}
