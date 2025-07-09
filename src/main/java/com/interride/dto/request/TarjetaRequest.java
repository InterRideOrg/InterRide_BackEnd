package com.interride.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TarjetaRequest(
        @NotBlank
        String numeroTarjeta,

        @NotBlank
        String nombreTitular,

        @NotBlank
        String fechaExpiracion,

        @NotBlank
        String codigoSeguridad
) { }
