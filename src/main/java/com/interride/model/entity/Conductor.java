package com.interride.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "conductor")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conductor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombres", nullable = false, columnDefinition = "TEXT")
    private String nombre;

    @Column(name = "apellidos", nullable = false, columnDefinition = "TEXT")
    private String apellidos;

    @Column(name = "correo", nullable = false, columnDefinition = "TEXT")
    private String correo;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "username", nullable = false, columnDefinition = "TEXT")
    private String username;

    @Column(name = "fecha_hora_registro", nullable = false)
    private LocalDateTime fechaHoraRegistro;


    @JsonIgnore
    @OneToOne(mappedBy = "conductor", cascade = CascadeType.ALL)
    private Vehiculo vehiculo;

}
