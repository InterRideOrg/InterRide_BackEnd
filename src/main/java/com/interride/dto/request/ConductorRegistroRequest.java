package com.interride.dto.request;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;

public record ConductorRegistroRequest(
        @NotBlank(message = "Nombre requerido")
        String nombre,

        @NotBlank(message = "Apellido requerido")
        String apellidos,

        @Email(message = "Correo inválido")
        String correo,

        @NotBlank(message = "Teléfono requerido")
        String telefono,

        @NotBlank(message = "Username requerido")
        String username,

        @NotBlank(message = "Contraseña requerida")
        String password
) {}
