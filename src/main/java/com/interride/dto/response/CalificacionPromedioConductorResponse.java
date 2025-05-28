package com.interride.dto.response;

import java.util.List;

public record CalificacionPromedioConductorResponse(
        Integer conductorId,
        Double calificacionPromedio,
        List<CalificacionResponse> calificaciones
) { }
