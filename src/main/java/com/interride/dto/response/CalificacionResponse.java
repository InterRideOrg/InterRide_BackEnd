package com.interride.dto.response;

public record CalificacionResponse(
    Integer id,
    Integer estrellas,
    String comentario,
    Integer viajeId,
    Integer conductorId,
    Integer pasajeroId
){}
