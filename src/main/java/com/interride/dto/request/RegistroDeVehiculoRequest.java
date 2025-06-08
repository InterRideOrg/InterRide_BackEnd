package com.interride.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistroDeVehiculoRequest {
    @NotBlank(message = "La placa es obligatoria")
    private String placa;
    @NotBlank(message = "La marca es obligatoria")
    private String marca;
    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;
    @NotBlank(message = "El año es obligatorio")
    private Integer anio;
    @NotBlank(message = "La cantidad de asientos es obligatoria")
    private Integer cantidadAsientos;
}
