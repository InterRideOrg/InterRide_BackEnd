package com.interride.repository;

import com.interride.model.entity.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasajeroRepository extends JpaRepository<Pasajero, Integer> {
    boolean existsByTelefono(String telefono);
    boolean existsByUsername(String username);
    boolean existsByUsernameAndUsuarioIdNot(String username, Integer usuarioId);

    Optional<Pasajero> findByUsername(String username);

    Optional<Pasajero> findByUsernameIgnoreCase(String username);
}
