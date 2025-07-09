package com.interride.model.entity;


import com.interride.model.enums.EstadoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pago")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPago estado;

    @Column(name = "fecha_hora_pago", nullable = false)
    private LocalDateTime fechaHoraPago;

    @Column(name = "monto", nullable = false)
    private Double monto;

    @ManyToOne
    @JoinColumn(name = "pasajero_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_pago_pasajero"))
    private Pasajero pasajero;

    @ManyToOne
    @JoinColumn(name = "conductor_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_pago_conductor"))
    private Conductor conductor;

    @ManyToOne
    @JoinColumn(name = "viaje_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_pago_viaje"))
    private Viaje viaje;

    @ManyToOne
    @JoinColumn(name = "tarjeta_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_pago_tarjeta"))
    private Tarjeta tarjeta;

}
