package com.interride.dto.response;

public record VehiculoResponse(
        String placa,
        String marca,
        String modelo,
        Integer anio,
        Integer cantidadAsientos
) {}