package com.interride.repository;

import com.interride.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario getUsuarioById(Integer id);
    boolean existsByCorreo(String correo);
    Optional<Usuario> findByCorreo(String correo);
}
