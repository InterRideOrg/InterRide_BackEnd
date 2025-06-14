package com.interride.dto.response;

public record UsuarioResponse(
        Integer id,
        String nombre,
        String apellidos,
        String correo,
        String telefono,
        String username,
        String fechaHoraRegistro,
        String fecheHoraActualizacion,
        String role
) {
}
