package com.interride.dto;

import lombok.Data;

@Data
public class VehiculoRequest {
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private Integer cantidadAsientos;
    private Integer conductorId;
}
