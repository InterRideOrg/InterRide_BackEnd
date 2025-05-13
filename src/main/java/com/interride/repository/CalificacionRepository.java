package com.interride.repository;

import com.interride.model.entity.Calificacion;
import com.interride.model.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {
    @Query("SELECT c FROM Calificacion c WHERE c.viaje.id = :id")
    List<Calificacion> findByViajeId(@Param("id") Integer id);

    @Query("SELECT c FROM Calificacion c WHERE c.conductor.id = :id")
    List<Calificacion> findByConductorId(@Param("id") Integer id);


}
