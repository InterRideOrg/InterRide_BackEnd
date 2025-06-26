package com.interride.service;

import com.interride.dto.request.RegistroDeVehiculoRequest;
import com.interride.dto.request.VehiculoRequest;
import com.interride.dto.response.VehiculoResponse;
import com.interride.model.entity.Vehiculo;

public interface VehiculoService {
    VehiculoResponse update(Integer conductorId, VehiculoRequest vehiculo);
    VehiculoResponse registrar(Integer usuarioId, RegistroDeVehiculoRequest registroDeVehiculoRequest);
}
