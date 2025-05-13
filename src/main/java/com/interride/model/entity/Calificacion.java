package com.interride.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "calificacion")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estrellas", nullable = false)
    private Integer estrellas;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "conductor_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_calificacion_conductor"))
    private Conductor conductor;

    @ManyToOne
    @JoinColumn(name = "viaje_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_calificacion_viaje"))
    private Viaje viaje;

    @ManyToOne
    @JoinColumn(name = "pasajero_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_calificacion_pasajero"))
    private Pasajero pasajero;
}
