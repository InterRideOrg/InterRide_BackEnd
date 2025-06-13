package com.interride.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateVehiculoRequest(
        @NotBlank(message = "Placa requerido")
        String placa,

        @NotBlank(message = "Marca requerido")
        String marca,

        @NotBlank(message = "Modelo requerido")
        String modelo,

        @NotNull(message = "Anio requerido")
        Integer anio,

        @NotNull(message = "Cantidad de asientos requerido")
        Integer cantidadAsientos

) {}
