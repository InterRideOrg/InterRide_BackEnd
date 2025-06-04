package com.interride.dto.response;

public record BoletoUnionResponse(
        Integer id,
        String fechaHoraUnion,
        Double costo,
        String fechaHoraLlegada,
        Integer pasajeroId,
        Integer viajeId,
        String estado,
        String direccionOrigen,
        String direccionDestinO,
        String provinciaOrigen,
        String provinciaDestino
) { }
