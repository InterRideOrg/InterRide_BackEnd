package com.interride.dto.response;

public record NotificacionPasajeroResponse(
        Integer id,
        String mensaje,
        String fechaHora,
        Boolean leido,
        Integer pasajeroId
) { }
