package com.interride.dto.response;

public record NotificacionResponse(
        Integer id,
        String mensaje,
        Integer idPasajero,
        Integer idConductor
) { }
