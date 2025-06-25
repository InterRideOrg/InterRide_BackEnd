package com.interride.service;

import com.interride.dto.request.UbicacionRequest;
import com.interride.dto.response.*;

public interface PasajeroViajeService {
    BoletoCanceladoResponse cancelarBoleto(Integer id);
    BoletoUnionResponse createBoletoUnion(Integer pasajeroId,
                                          Integer viajeId,
                                          Integer asientosOcupados,
                                          UbicacionRequest request);

    BoletoCompletadoResponse finalizarBoleto(Integer id);
    BoletoAbordoResponse abordarViaje(Integer pasajeroId, Integer viajeId);
    BoletoResponse getBoletoByPasajeroIdAndViajeId(Integer pasajeroId, Integer viajeId);
}
