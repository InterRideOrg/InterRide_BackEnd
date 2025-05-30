package com.interride.dto.response;

import com.interride.model.enums.EstadoViaje;

import java.time.LocalDateTime;

public record ViajeCanceladoResponse(
        Integer id,
        EstadoViaje estadoViaje,
        String provinciaOrigen,
        String provinciaDestino,
        LocalDateTime fechaHoraPartida,
        LocalDateTime fechaHoraCancelacion
) { }
