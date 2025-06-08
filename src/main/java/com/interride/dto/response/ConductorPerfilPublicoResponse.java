package com.interride.dto.response;

public record ConductorPerfilPublicoResponse(
        String nombres,
        String apellidos,
        String correo,
        String telefono,
        String placaVehiculo
) {}
