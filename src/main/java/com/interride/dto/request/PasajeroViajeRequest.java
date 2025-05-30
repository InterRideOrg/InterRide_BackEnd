package com.interride.dto.request;

import jakarta.validation.constraints.NotNull;

public record PasajeroViajeRequest(
        @NotNull(message = "La cantidad de asientos ocupados no puede ser nula")
        Integer asientosOcupados,

        @NotNull(message = "El ID del pasajero no puede ser nulo")
        Integer idPasajero,

        @NotNull(message = "El ID del viaje no puede ser nulo")
        Integer idViaje
) { }
