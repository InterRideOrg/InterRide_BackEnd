package com.interride.repository;

import com.interride.model.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConductorRepository extends JpaRepository<Conductor, Integer> {
    Optional<Conductor> findById(Integer id);
 
    boolean existsByTelefono(String telefono);
    boolean existsByUsername(String username);
    boolean existsByUsernameAndUsuarioIdNot(String username, Integer usuarioId);

}
