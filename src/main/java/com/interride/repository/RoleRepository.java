package com.interride.repository;

import com.interride.model.entity.Role;
import com.interride.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    //buscar rol por nombre
    Optional<Role> findByNombre(ERole nombre);
}
