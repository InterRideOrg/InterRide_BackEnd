package com.interride.repository;

import com.interride.model.entity.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasajeroRepository extends JpaRepository<Pasajero, Integer> {
    boolean existsByCorreo(String correo);
    boolean existsByTelefono(String telefono);
}
