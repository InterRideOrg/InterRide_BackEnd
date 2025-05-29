package com.interride.repository;

import com.interride.model.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {

    @Query("SELECT u FROM Ubicacion u WHERE u.viaje.id = :id")
    Ubicacion findByViajeId(@Param("id") Integer id);
}
