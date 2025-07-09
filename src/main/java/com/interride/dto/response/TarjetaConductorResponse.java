package com.interride.dto.response;

public record TarjetaConductorResponse(
        Integer id,
        String numeroTarjeta,
        String nombreTitular,
        String fechaExpiracion,
        String codigoSeguridad,
        Integer conductorId
) { }
