package com.interride.dto.response;

public record BoletoCanceladoResponse(
        Integer id,
        Integer viajeId,
        String provinciaOrigen,
        String provinciaDestino,
        String fechaHoraPartida,
        Double costo,
        String estado
) { }
