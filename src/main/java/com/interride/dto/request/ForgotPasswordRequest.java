// src/main/java/com/interride/dto/ForgotPasswordRequest.java
package com.interride.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;   // <- si usas Lombok

@Data
@Getter @Setter       // <- crea getCorreo()/setCorreo()
public class ForgotPasswordRequest {

    @NotBlank @Email
    private String correo;
    private String password;
}
