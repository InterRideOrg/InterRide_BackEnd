package com.interride.service;

import com.interride.dto.request.UbicacionRequest;
import com.interride.dto.response.*;
import com.interride.model.enums.EstadoViaje;

import java.util.List;

public interface PasajeroViajeService {
    BoletoCanceladoResponse cancelarBoleto(Integer id);
    BoletoUnionResponse createBoletoUnion(Integer pasajeroId,
                                          Integer viajeId,
                                          Integer asientosOcupados,
                                          UbicacionRequest request);

    BoletoCompletadoResponse finalizarBoleto(Integer id);
    BoletoAbordoResponse abordarViaje(Integer pasajeroId, Integer viajeId);
    BoletoResponse getBoletoByPasajeroIdAndViajeId(Integer pasajeroId, Integer viajeId);
    List<BoletoResponse> getBoletosByPasajeroIdAndState(Integer pasajeroId, EstadoViaje state);

    List<BoletoResponse> getBoletosByViajeId(Integer viajeId);
}
