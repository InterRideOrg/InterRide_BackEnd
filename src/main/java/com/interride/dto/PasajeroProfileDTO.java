package com.interride.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasajeroProfileDTO {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private String username;
    private String fechaHoraRegistro;
}
