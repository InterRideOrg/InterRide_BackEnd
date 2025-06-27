package com.interride.dto.request;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public record CreateCalificacionRequest(
        @NotNull(message = "La cantidad de estrellas son obligatoria")
        Integer estrellas,

        @Size(max = 255, message = "El comentario no puede superar los 255 caracteres")
        String comentario,

        @NotNull(message = "El ID del viaje es obligatorio")
        Integer viajeId,

        @NotNull(message = "El ID del conductor es obligatorio")
        Integer conductorId,

        @NotNull(message = "El ID del pasajero es obligatorio")
        Integer pasajeroId
) {}
