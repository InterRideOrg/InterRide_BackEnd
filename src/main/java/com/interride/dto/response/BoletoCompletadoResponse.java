package com.interride.dto.response;

import com.interride.model.enums.EstadoViaje;

public record BoletoCompletadoResponse(
        Integer id,
        String fechaHoraLlegada,
        Double costo,
        EstadoViaje estado,
        Integer pasajeroId,
        Integer viajeId,
        String mensaje
) { }
