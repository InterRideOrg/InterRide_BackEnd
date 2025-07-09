package com.interride.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;

public record VehiculoRequest(
        @NotBlank(message = "Placa requerido")
        String placa,

        @NotBlank(message = "Marca requerido")
        String marca,

        @NotBlank(message = "Modelo requerido")
        String modelo,

        @NotNull(message = "Anio requerido")
        Integer anio,

        @NotNull(message = "Cantidad de asientos requerido")
        @Max(message = "La cantidad de asientos no puede ser mayor a 8", value = 8)
        @Positive(message = "La cantidad de asientos debe ser un número positivo")
        Integer cantidadAsientos

) {}
