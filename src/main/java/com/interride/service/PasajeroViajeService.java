package com.interride.service;

import com.interride.dto.request.UbicacionRequest;
import com.interride.dto.response.BoletoCanceladoResponse;
import com.interride.dto.response.BoletoCompletadoResponse;
import com.interride.dto.response.BoletoUnionResponse;

public interface PasajeroViajeService {
    BoletoCanceladoResponse cancelarBoleto(Integer id);
    BoletoUnionResponse createBoletoUnion(Integer pasajeroId,
                                          Integer viajeId,
                                          Integer asientosOcupados,
                                          UbicacionRequest request);

    BoletoCompletadoResponse finalizarBoleto(Integer id);
}
