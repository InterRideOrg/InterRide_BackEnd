package com.interride.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ActualizarConductorPerfilRequest {
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
}
