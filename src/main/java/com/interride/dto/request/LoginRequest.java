package com.interride.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Email(message = "El correo electrónico no es válido")
    @NotBlank(message = "El correo electrónico es obligatorio")
    private String correo;     // **coincide con Pasajero.correo**

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}