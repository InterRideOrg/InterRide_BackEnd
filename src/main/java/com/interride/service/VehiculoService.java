package com.interride.service;

import com.interride.dto.request.RegistroDeVehiculoRequest;
import com.interride.dto.request.UpdateVehiculoRequest;
import com.interride.model.entity.Vehiculo;

public interface VehiculoService {
    Vehiculo update(Integer conductorId, UpdateVehiculoRequest vehiculo);
    Vehiculo registrar(Integer conductorId, RegistroDeVehiculoRequest registroDeVehiculoRequest);
}
