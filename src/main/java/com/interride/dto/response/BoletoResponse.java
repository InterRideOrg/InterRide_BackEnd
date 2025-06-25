package com.interride.dto.response;

public record BoletoResponse(
        Integer boletoId,
        Integer viajeId,
        Integer pasajeroId,
        String fechaHoraLlegada,
        String fechaHoraUnion,
        Integer asientosOcupados,
        Double costo,
        String estado,
        Boolean abordo,
        String provinciaOrigen,
        String provinciaDestino,
        String direccionPartida,
        String direccionDestino
) { }
