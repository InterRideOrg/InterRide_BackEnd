package com.interride.model.entity;

import com.interride.model.enums.EstadoViaje;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pasajero_viaje")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasajeroViaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_hora_union", nullable = false)
    private LocalDateTime fechaHoraUnion;

    @Column(name = "fecha_hora_llegada", nullable = false)
    private LocalDateTime fechaHoraLLegada;

    @Column(name = "costo", nullable = false)
    private Double costo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoViaje estado;

    @Column(name = "asientos_ocupados", nullable = false)
    private Integer asientosOcupados;

    @ManyToOne
    @JoinColumn(name = "pasajero_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_pasajeroViaje_pasajero"))
    private Pasajero pasajero;

    @ManyToOne
    @JoinColumn(name = "viaje_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_pasajeroViaje_viaje"))
    private Viaje viaje;
}
