package com.interride.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UbicacionRequest(
        @NotNull(message = "La latitud es obligatoria")
        BigDecimal latitud,

        @NotNull(message = "La longitud es obligatoria")
        BigDecimal longitud,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
        String direccion,

        @NotBlank(message = "La provincia es obligatoria")
        @Size(max = 100, message = "La provincia no puede superar los 100 caracteres")
        String provincia

) { }
