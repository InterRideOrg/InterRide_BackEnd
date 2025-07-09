package com.interride.dto.response;

public record TarjetaPasajeroResponse(
        Integer id,
        String numeroTarjeta,
        String nombreTitular,
        String fechaExpiracion,
        String codigoSeguridad,
        Integer pasajeroId
) { }
