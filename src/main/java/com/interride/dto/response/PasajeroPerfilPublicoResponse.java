package com.interride.dto.response;

import java.time.LocalDateTime;

public record PasajeroPerfilPublicoResponse(
    String nombres,
    String apellidos,
    String correo,
    String telefono,
    String username
){}
