package com.interride.dto.response;

import com.interride.model.enums.EstadoViaje;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PasajeroViajesResponse {

    private Integer viaje_id;
    private LocalDateTime fecha_hora_partida;
    private EstadoViaje estado;
    private String conductor_nombres;
    private String conductor_apellidos;
    private LocalDateTime fecha_hora_union;
    private LocalDateTime fecha_hora_llegada;
    private Double costo;

    public PasajeroViajesResponse(Integer viaje_id, LocalDateTime fecha_hora_partida, EstadoViaje estado, String conductor_nombres, String conductor_apellidos, LocalDateTime fecha_hora_union, LocalDateTime fecha_hora_llegada, Double costo) {
        this.viaje_id = viaje_id;
        this.fecha_hora_partida = fecha_hora_partida;
        this.estado = estado;
        this.conductor_nombres = conductor_nombres;
        this.conductor_apellidos = conductor_apellidos;
        this.fecha_hora_union = fecha_hora_union;
        this.fecha_hora_llegada = fecha_hora_llegada;
        this.costo = costo;
    }
}