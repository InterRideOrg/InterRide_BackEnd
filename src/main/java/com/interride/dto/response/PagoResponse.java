package com.interride.dto.response;

import com.interride.model.enums.EstadoPago;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PagoResponse(
        Integer id,
        EstadoPago estado,
        LocalDateTime fechaHoraPago,
        Double monto,
        Integer pasajeroId,
        Integer conductorId,
        Integer viajeId
) { }
