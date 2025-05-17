package com.interride.dto.response;

import java.time.LocalDateTime;

public record ReclamoResponse(
        Integer id,
        String mensaje,
        LocalDateTime fechaHoraEnvio
) {}
