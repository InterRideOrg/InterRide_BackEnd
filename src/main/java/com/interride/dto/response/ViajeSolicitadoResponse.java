package com.interride.dto.response;

public record ViajeSolicitadoResponse (
        Integer id,
        String fechaHoraPartida,
        String fechaHoraLlegada,
        String provinciaOrigen,
        String provinciaDestino,
        String direccionOrigen,
        String direccionDestino,
        Double costo,
        Integer asientosReservados,
        String estado
)
{ }
