package com.interride.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tarjeta")
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_tarjeta", nullable = false)
    private String numeroTarjeta;

    @Column(name = "nombre_titular", nullable = false, columnDefinition = "TEXT")
    private String nombreTitular;

    @Column(name = "correo", nullable = false, columnDefinition = "TEXT")
    private String correo;

    @Column(name = "fecha_vencimiento", nullable = false)
    private String fechaVencimiento;

    @Column(name = "cvv", nullable = false)
    private String cvv;

    @Column(name = "saldo", nullable = false)
    private Double saldo;

    @ManyToOne
    @JoinColumn(name = "conductor_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_tarjeta_conductor"))
    private Conductor conductor;

    @ManyToOne
    @JoinColumn(name = "pasajero_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_tarjeta_pasajero"))
    private Pasajero pasajero;
}
