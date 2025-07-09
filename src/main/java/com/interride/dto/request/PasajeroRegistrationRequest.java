package com.interride.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PasajeroRegistrationRequest {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @Email(message = "El correo electronico no es válido")
    @NotBlank(message = "El correo no puede estar vacío")
    private String correo;

    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    @NotNull(message = "La contraseña no puede estar vacía")
    private String password;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;
}
