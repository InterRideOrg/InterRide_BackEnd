package com.interride.dto.response;

import java.time.LocalDateTime;

public record ViajeDisponibleResponse(
        Integer viajeId,
        Integer conductorId,
        String provinciaOrigen,
        String provinciaDestino,
        LocalDateTime fechaHoraPartida,
        String direccionPartida,
        Integer asientosDisponibles
)
{ }
