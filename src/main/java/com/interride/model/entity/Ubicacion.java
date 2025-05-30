package com.interride.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ubicacion")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "latitud", nullable = false, precision = 10, scale = 8, columnDefinition = "DECIMAL(10,8)")
    private BigDecimal latitud;

    @Column(name = "longitud", nullable = false, precision = 10, scale = 8, columnDefinition = "DECIMAL(10,8)")
    private BigDecimal longitud;

    @Column(name = "direccion", nullable = false, columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "provincia", nullable = false, columnDefinition = "TEXT")
    private String provincia;

    @OneToOne
    @JoinColumn(name = "viaje_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_ubicacion_viaje"))
    private Viaje viaje;

    @OneToOne
    @JoinColumn(name = "pasajero_viaje_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_ubicacion_pasajero_viaje"))
    private PasajeroViaje pasajeroViaje;
}