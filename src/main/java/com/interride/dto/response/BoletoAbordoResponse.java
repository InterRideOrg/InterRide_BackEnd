package com.interride.dto.response;

public record BoletoAbordoResponse(
        Integer boletoId,
        Integer asientosReservados,
        Boolean abordo
) { }
