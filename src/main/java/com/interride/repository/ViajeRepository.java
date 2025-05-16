package com.interride.repository;

import com.interride.model.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ViajeRepository extends JpaRepository<Viaje, Integer> {
    @Query(value= """
        SELECT
            v.id AS viaje_id,
            v.fecha_hora_partida,
            v.estado,
            c.nombres AS conductor_nombres,
            c.apellidos AS conductor_apellidos,
            pv.fecha_hora_union,
            pv.fecha_hora_llegada,
            pv.costo
        FROM pasajero_viaje pv
        JOIN viaje v ON pv.viaje_id = v.id
        LEFT JOIN conductor c ON v.conductor_id = c.id
        WHERE pv.pasajero_id = :pasajeroid;
""",nativeQuery=true)
    List<Object[]> getViajesByPasajeroId(@Param("pasajeroid") Integer pasajeroid);
}
