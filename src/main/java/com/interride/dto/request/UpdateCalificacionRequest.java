package com.interride.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateCalificacionRequest(
        @Min(value = 1, message = "Las estrellas deben ser mínimo 1")
        @Max(value = 5, message = "Las estrellas deben ser máximo 5")
        Integer estrellas,

        @Size(max = 255, message = "El comentario no puede superar los 255 caracteres")
        String comentario
) {}
