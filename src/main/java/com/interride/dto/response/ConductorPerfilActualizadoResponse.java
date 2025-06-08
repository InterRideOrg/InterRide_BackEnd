package com.interride.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConductorPerfilActualizadoResponse {
    String nombres;
    String apellidos;
    String correo;
    String telefono;
    String username;
}
