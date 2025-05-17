package com.interride.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PasajeroRegistrationRequest {
    @NotBlank private String nombre;
    @NotBlank private String apellidos;
    @Email   @NotBlank private String correo;
    @Size(min = 4) @NotBlank private String password;
    @NotBlank private String telefono;
    @NotBlank private String username;
}
