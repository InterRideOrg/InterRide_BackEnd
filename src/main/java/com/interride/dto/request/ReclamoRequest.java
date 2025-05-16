package com.interride.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReclamoRequest(
        @NotBlank(message = "El mensaje no puede estar vacío.")
        String mensaje,

        Integer idPasajero, // puede ser null si es conductor
        Integer idConductor // puede ser null si es pasajero
) {}
