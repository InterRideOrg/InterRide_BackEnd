package com.interride.dto.response;

public record PasajeroPerfilPrivadoResponse(
        String nombres,
        String apellidos,
        String telefono,
        String correo,
        String username
) {
}
