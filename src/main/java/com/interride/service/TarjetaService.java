package com.interride.service;

import com.interride.dto.request.TarjetaRequest;
import com.interride.dto.response.TarjetaPasajeroResponse;

import java.util.List;

public interface TarjetaService {
    TarjetaPasajeroResponse createTarjetaPasajero(Integer idPasajero, TarjetaRequest request);
    List<TarjetaPasajeroResponse> getTarjetasPasajero(Integer idPasajero);
    void deleteTarjetaPasajero(Integer idTarjeta);

}
