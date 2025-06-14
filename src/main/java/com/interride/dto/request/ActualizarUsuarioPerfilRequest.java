package com.interride.dto.request;

import jakarta.validation.constraints.NotBlank;


public record ActualizarUsuarioPerfilRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombres,

        @NotBlank(message = "Los apellidos no pueden estar vacíos")
        String apellidos,

        @NotBlank(message = "El número de teléfono no puede estar vacío")
        String telefono,

        @NotBlank(message = "El correo electrónico no puede estar vacío")
        String correo,

        @NotBlank(message = "El nombre de usuario no puede estar vacío")
        String username
) { }
