package com.interride.dto.response;

import com.interride.model.enums.ERole;

public record ConductorRegistroResponse(
        Integer id,
        String nombre,
        String apellidos,
        String correo,
        String telefono,
        String username,
        String fechaHoraRegistro,
        ERole role
) { }
