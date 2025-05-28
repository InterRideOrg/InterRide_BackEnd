package com.interride.dto.response;

import java.time.LocalDateTime;

public record ViajeCompletadoResponse (
        Integer idViaje,
        String provinciaOrigen,
        String provinciaDestino,
        String direccionOrigen,
        LocalDateTime fechaHoraPartida,
        Double gananciaTotal,
        Double calificacionConductor
) {}
