package com.interride.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehiculo")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "placa", nullable = false)
    private String placa;

    @Column(name = "marca", nullable = false)
    private String marca;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "cantidad_asientos", nullable = false)
    private Integer cantidadAsientos;

    @OneToOne
    @JoinColumn(name = "conductor_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_vehiculo_conductor"))
    private Conductor conductor;
}
