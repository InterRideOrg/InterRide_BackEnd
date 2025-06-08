package com.interride.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ViajeCompletadoConductorResponse(
        List<String> nombresPasajeros,
        LocalDateTime fechaHora,
        String direccionOrigen,
        String provinciaDestino,
        Integer asientosUtilizados,
        double costoTotal,
        double calificacionPromedio
) {}
