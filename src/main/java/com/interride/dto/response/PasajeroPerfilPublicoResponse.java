package com.interride.dto.response;

import java.time.LocalDateTime;

public record PasajeroPerfilPublicoResponse(
    String nombres,
    String apellidos,
    String telefono,
    String username
){}
