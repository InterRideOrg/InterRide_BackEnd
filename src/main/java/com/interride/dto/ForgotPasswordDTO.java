// src/main/java/com/interride/dto/ForgotPasswordDTO.java
package com.interride.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;   // <- si usas Lombok

@Data
@Getter @Setter       // <- crea getCorreo()/setCorreo()
public class ForgotPasswordDTO {

    @NotBlank @Email
    private String correo;
    private String password;
}
