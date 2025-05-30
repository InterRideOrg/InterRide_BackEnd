package com.interride.dto.response;

public record NotificacionConductorResponse(
        Integer id,
        String mensaje,
        String fechaHora,
        Boolean leido,
        Integer conductorId
) { }
