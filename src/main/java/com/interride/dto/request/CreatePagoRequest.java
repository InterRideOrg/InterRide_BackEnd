package com.interride.dto.request;

import com.interride.model.enums.EstadoPago;
import jakarta.validation.constraints.NotNull;


public record CreatePagoRequest(
        @NotNull(message = "El estado del pago es obligatorio")
        EstadoPago estado,

        @NotNull(message = "El monto del pago es obligatorio")
        Double monto,

        @NotNull(message = "El ID del pasajero es obligatorio")
        Integer pasajeroId,

        @NotNull(message = "El ID del conductor es obligatorio")
        Integer conductorId,

        @NotNull(message = "El ID del viaje es obligatorio")
        Integer viajeId
) { }
