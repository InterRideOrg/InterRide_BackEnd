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

    @Query(value = """
        SELECT
            v.fecha_hora_partida,
            u1.direccion as origen,
            u2.direccion as destino,
            CONCAT(c.nombres, ' ', c.apellidos) AS conductor_nombre,
            'Tarjeta' as modo_pago,
            pv.costo,
            cal.estrellas,
            v.estado
        FROM viaje v
        JOIN pasajero_viaje pv ON pv.viaje_id = v.id
        JOIN ubicacion u1 ON u1.viaje_id = v.id
        JOIN ubicacion u2 ON u2.id = pv.ubicacion_id
        LEFT JOIN conductor c ON v.conductor_id = c.id
        LEFT JOIN calificacion cal ON cal.viaje_id = v.id AND cal.pasajero_id = pv.pasajero_id
        WHERE v.id = :idViaje AND pv.pasajero_id = :idPasajero
        """, nativeQuery = true)
    List<Object[]> getDetalleViajeById(@Param("idViaje") Integer idViaje, @Param("idPasajero") Integer idPasajero);

    @Query(value = """
    SELECT
        v.fecha_hora_partida,
        u1.direccion as origen,
        u2.direccion as destino,
        CONCAT(c.nombres, ' ', c.apellidos) AS conductor_nombre,
        v.estado
    FROM viaje v
    LEFT JOIN pasajero_viaje pv ON pv.viaje_id = v.id
    LEFT JOIN ubicacion u1 ON u1.viaje_id = v.id
    LEFT JOIN ubicacion u2 ON u2.id = pv.ubicacion_id
    LEFT JOIN conductor c ON v.conductor_id = c.id
    WHERE v.id = :idViaje AND v.estado = 'CANCELADO'
    """, nativeQuery = true)
    List<Object[]> getDetalleViajeCancelado(@Param("idViaje") Integer idViaje);

    // return bool: true si el viaje esta cancelado
    @Query(value = """
        SELECT CASE WHEN v.estado = 'CANCELADO' THEN true ELSE false END
        FROM viaje v
        WHERE v.id = :idViaje
    """, nativeQuery = true)
    Boolean isViajeCancelado(@Param("idViaje") Integer idViaje);

}