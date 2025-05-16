package com.interride.dto.request;

import com.interride.model.enums.EstadoPago;
import jakarta.validation.constraints.NotNull;

public record UpdatePagoRequest (
        @NotNull(message = "El estado del pago es obligatorio")
        EstadoPago estadoPago
){}
