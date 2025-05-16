package com.interride.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank
    private String login;      // correo o username
    @NotBlank
    private String password;
}
