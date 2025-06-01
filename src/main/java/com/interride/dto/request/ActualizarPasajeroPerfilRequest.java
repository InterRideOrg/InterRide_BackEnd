package com.interride.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActualizarPasajeroPerfilRequest {
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
}
