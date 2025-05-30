package com.interride.service;

import com.interride.dto.response.BoletoCanceladoResponse;

public interface PasajeroViajeService {
    BoletoCanceladoResponse ObtenerViajeCanceladoById(Integer id);
}
