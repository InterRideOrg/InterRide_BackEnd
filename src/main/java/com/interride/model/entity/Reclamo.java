package com.interride.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reclamo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reclamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_hora_envio", nullable = false)
    private LocalDateTime fechaHoraEnvio;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "conductor_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_reclamo_conductor"))
    private Conductor conductor;

    @ManyToOne
    @JoinColumn(name = "pasajero_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_reclamo_pasajero"))
    private Pasajero pasajero;
}
