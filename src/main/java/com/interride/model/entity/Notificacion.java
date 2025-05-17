package com.interride.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notificacion")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_hora_envio", nullable = false)
    private LocalDateTime fechaHoraEnvio;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "leido", nullable = false)
    private Boolean leido;

    @ManyToOne
    @JoinColumn(name = "pasajero_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_notificacion_pasajero"))
    private Pasajero pasajero;

    @ManyToOne
    @JoinColumn(name = "conductor_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_notificacion_conductor"))
    private Conductor conductor;

}
