package com.interride.repository;

import com.interride.model.entity.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasajeroRepository extends JpaRepository<Pasajero, Integer> {
    boolean existsByCorreo(String correo);
    boolean existsByTelefono(String telefono);
    Optional<Pasajero> findByUsername(String username);
    Optional<Pasajero> findByCorreoOrUsername(String correo, String username);
    Optional<Pasajero> findByCorreo(String correo);
    Optional<Pasajero> findByCorreoIgnoreCase(String correo);
    Optional<Pasajero> findByUsernameIgnoreCase(String username);
}
