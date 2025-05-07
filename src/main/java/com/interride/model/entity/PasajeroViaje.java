package com.interride.model.entity;

import com.interride.model.enums.EstadoViaje;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pasajero_viaje")
public class PasajeroViaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_hora_union", nullable = false)
    private LocalDateTime fechaHoraUnion;

    @Column(name = "fecha_hora_llegada", nullable = false)
    private LocalDateTime fechaHoraLLegada;

    @Column(name = "costo", nullable = false)
    private BigDecimal costo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoViaje estado;

    @ManyToOne
    @JoinColumn(name = "pasajero_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_pasajeroViaje_pasajero"))
    private Pasajero pasajero;

    @ManyToOne
    @JoinColumn(name = "viaje_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_pasajeroViaje_viaje"))
    private Viaje viaje;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_pasajeroViaje_ubicacion"))
    private Ubicacion ubicacion;
}
