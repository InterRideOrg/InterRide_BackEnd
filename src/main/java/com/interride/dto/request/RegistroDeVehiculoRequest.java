package com.interride.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RegistroDeVehiculoRequest {
    @NotBlank(message = "La placa es obligatoria")
    private String placa;
    @NotBlank(message = "La marca es obligatoria")
    private String marca;
    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;
    @NotNull(message = "El año es obligatorio")
    private Integer anio;
    @Max(message = "La cantidad de asientos no puede ser mayor a 8", value = 8)
    @Positive(message = "La cantidad de asientos debe ser un número positivo")
    private Integer cantidadAsientos;
}
