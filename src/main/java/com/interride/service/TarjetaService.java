package com.interride.service;

import com.interride.dto.request.TarjetaRequest;
import com.interride.dto.response.TarjetaConductorResponse;
import com.interride.dto.response.TarjetaPasajeroResponse;

import java.util.List;

public interface TarjetaService {
    TarjetaPasajeroResponse createTarjetaPasajero(Integer idPasajero, TarjetaRequest request);
    List<TarjetaPasajeroResponse> getTarjetasPasajero(Integer idPasajero);

    TarjetaConductorResponse createTarjetaConductor(Integer idConductor, TarjetaRequest request);
    TarjetaConductorResponse getTarjetaConductorById(Integer idConductor);

    void delete(Integer idTarjeta);
}
