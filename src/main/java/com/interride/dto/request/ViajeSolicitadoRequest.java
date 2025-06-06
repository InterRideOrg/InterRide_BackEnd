package com.interride.dto.request;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ViajeSolicitadoRequest(
        @NotBlank(message = "La fecha y hora de partida es obligatoria")
        String fechaHoraPartida,

        @NotNull(message = "La cantidad de asientos pedidos es obligatoria")
        Integer asientosReservados,

        @NotNull(message = "La latitud de origen es obligatoria")
        BigDecimal latitudOrigen,

        @NotNull(message = "La longitud de origen es obligatoria")
        BigDecimal longitudOrigen,

        @NotBlank(message = "La provincia de origen es obligatoria")
        String provinciaOrigen,

        @NotBlank(message = "La dirección de origen es obligatoria")
        String direccionOrigen,

        @NotNull(message = "La latitud de destino es obligatoria")
        BigDecimal latitudDestino,

        @NotNull(message = "La longitud de destino es obligatoria")
        BigDecimal longitudDestino,

        @NotBlank(message = "La provincia de destino es obligatoria")
        String provinciaDestino,

        @NotBlank(message = "La dirección de destino es obligatoria")
        String direccionDestino
) { }
