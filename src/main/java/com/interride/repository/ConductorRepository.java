package com.interride.repository;

import com.interride.model.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConductorRepository extends JpaRepository<Conductor, Integer> {
    boolean existsByCorreo(String correo);
    boolean existsByTelefono(String telefono);
    boolean existsByUsername(String username);

}
