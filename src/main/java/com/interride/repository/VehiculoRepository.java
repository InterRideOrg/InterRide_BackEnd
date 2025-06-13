package com.interride.repository;

import com.interride.model.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    Optional<Vehiculo> findByConductorId(Integer conductorId);

    // true or false if exists a vehicle for the given conductor ID
    @Query(value = """
            SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END
            FROM vehiculo v
            WHERE v.conductor_id = :id_conductor
            """, nativeQuery = true)
    boolean existsByConductorId(@Param("id_conductor") Integer conductorId);

    boolean existsByPlaca(String placa);
}
