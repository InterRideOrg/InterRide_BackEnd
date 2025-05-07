package com.interride.model.entity;

import com.interride.model.enums.EstadoViaje;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "viaje")
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_hora_creacion", nullable = false)
    private LocalDateTime fechaHoraCreacion;

    @Column(name = "fecha_hora_partida", nullable = false)
    private LocalDateTime fechaHoraPartida;

    @Column(name = "asientos_disponibles", nullable = false)
    private Integer asientosDisponibles;

    @Column(name = "asientos_ocupados", nullable = false)
    private Integer asientosOcupados;

    @Column(name = "estado", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoViaje estado;

    @ManyToOne
    @JoinColumn(name = "conductor_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_viaje_conductor"))
    private Conductor conductor;

}
