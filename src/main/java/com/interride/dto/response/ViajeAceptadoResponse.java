package com.interride.dto.response;

import com.interride.model.enums.EstadoViaje;

import java.time.LocalDateTime;

public record ViajeAceptadoResponse(
        Integer idViaje,
        Integer conductorId,
        EstadoViaje estadoViaje,
        String provinciaOrigen,
        String provinciaDestino,
        String direccionOrigen,
        LocalDateTime fechaHoraPartida,
        Integer asientosDisponibles
) {}
