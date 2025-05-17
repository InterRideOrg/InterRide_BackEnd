package com.interride.dto.response;

import com.interride.model.enums.EstadoViaje;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DetalleViajeResponse {
    private LocalDateTime fechaHora;
    private String origen;
    private String destino;
    private String conductorNombreCompleto;
    private String modoPago; // Ej: "Tarjeta"
    private Double montoPagado;
    private Integer calificacionEstrellas; // puede ser null
    private EstadoViaje estado;

    public DetalleViajeResponse(LocalDateTime fechaHora, String origen, String destino, String conductorNombreCompleto, String modoPago, Double montoPagado, Integer calificacionEstrellas, EstadoViaje estado) {
        this.fechaHora = fechaHora;
        this.origen = origen;
        this.destino = destino;
        this.conductorNombreCompleto = conductorNombreCompleto;
        this.modoPago = modoPago;
        this.montoPagado = montoPagado;
        this.calificacionEstrellas = calificacionEstrellas;
        this.estado = estado;
    }
}
