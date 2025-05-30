package com.interride.repository;

import com.interride.dto.response.ViajeCompletadoResponse;
import com.interride.model.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import com.interride.dto.response.ViajeDisponibleResponse;

import java.sql.Timestamp;
import java.util.Optional;


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

    @Query(value = """
    SELECT CASE 
        WHEN v.estado = 'CANCELADO' THEN TRUE
        ELSE FALSE
    END
    FROM viaje v
    WHERE v.id = :idViaje
    """, nativeQuery = true)
    Boolean isViajeCancelado(@Param("idViaje") Integer idViaje);



    @Query(value = """
    SELECT
        v.id AS viaje_id,
        c.nombres AS conductor_nombre,
        c.apellidos AS conductor_apellido,
        veh.modelo AS vehiculo_modelo,
        veh.placa AS vehiculo_placa,
        veh.marca AS vehiculo_marca,
        veh.cantidad_asientos,
        v.asientos_ocupados,
        ubi_origen.longitud AS origen_longitud,
        ubi_origen.latitud AS origen_latitud,
        ubi_origen.provincia AS origen_provincia,
        ubi_destino.longitud AS destino_longitud,
        ubi_destino.latitud AS destino_latitud,
        ubi_destino.provincia AS destino_provincia,
        v.estado,
        v.fecha_hora_partida
    FROM pasajero_viaje pv
    JOIN viaje v ON v.id = pv.viaje_id
    JOIN conductor c ON c.id = v.conductor_id
    JOIN vehiculo veh ON veh.conductor_id = c.id
    JOIN ubicacion ubi_origen ON ubi_origen.viaje_id = v.id
    JOIN ubicacion ubi_destino ON ubi_destino.id = pv.ubicacion_id
    WHERE pv.pasajero_id = :idPasajero
      AND v.estado = 'EN_CURSO';
    """, nativeQuery = true)
    List<Object[]> getViajeEnCursoById(@Param("idPasajero") Integer idPasajero);


    @Query(value = """
        SELECT CASE WHEN v.estado = 'EN_CURSO' THEN true ELSE false END
        FROM viaje v
        WHERE v.id = :idViaje
    """, nativeQuery = true)
    List<Object[]> isViajeEnCurso(@Param("idViaje") Integer idViaje);

    @Query(value = """
        SELECT pv.pasajero_id
        FROM pasajero_viaje pv
        WHERE pv.viaje_id = :id_viaje
    """, nativeQuery = true)
    List<Object[]> getPasajerosIdsByViajeId(@Param("id_viaje") Integer idViaje);

    @Query(value = """
        SELECT v.conductor_id
        FROM viaje v
        WHERE v.id = :id_viaje
        AND v.conductor_id IS NOT NULL
""", nativeQuery = true)
    Optional<Integer> getConductorIdByViajeId(@Param("id_viaje") Integer idViaje);


    @Query("SELECT NEW com.interride.dto.response.ViajeDisponibleResponse(" +
            "v.id, v.conductor.id,  uo.provincia, ud.provincia, v.fechaHoraPartida, " +
            "uo.direccion, v.asientosDisponibles) " +
            "FROM Viaje v " +
            "JOIN Ubicacion uo ON uo.viaje.id = v.id " + // Origen
            "JOIN PasajeroViaje pv ON pv.viaje.id = v.id " +
            "JOIN Ubicacion ud ON ud.id = pv.ubicacion.id " + // Destino
            "WHERE v.estado = com.interride.model.enums.EstadoViaje.ACEPTADO " +
            "AND v.asientosDisponibles > 0 " +
            "AND uo.provincia = :provinciaOrigen " +
            "AND ud.provincia = :provinciaDestino " +
            "AND DATE(v.fechaHoraPartida) = :fechaPartida")
    List<ViajeDisponibleResponse> findViajesDisponibles(
            @Param("provinciaOrigen") String provinciaOrigen,
            @Param("provinciaDestino") String provinciaDestino,
            @Param("fechaPartida") LocalDate fechaPartida
    );

    @Query("SELECT NEW com.interride.dto.response.ViajeCompletadoResponse(" +
            "v.id, uo.provincia, ud.provincia, uo.direccion, v.fechaHoraPartida, " +
            "SUM(pv.costo), AVG(c.estrellas)) " +
            "FROM Viaje v " +
            "JOIN Ubicacion uo ON uo.viaje.id = v.id " + // Origen
            "JOIN PasajeroViaje pv ON pv.viaje.id = v.id " +
            "JOIN Ubicacion ud ON ud.id = pv.ubicacion.id " + // Destino
            "JOIN Calificacion c ON c.id = v.id " +
            "WHERE v.estado = com.interride.model.enums.EstadoViaje.COMPLETADO " +
            "AND v.conductor.id = :idConductor " +
            "GROUP BY v.id , uo.provincia, ud.provincia, uo.direccion, v.fechaHoraPartida " +
            "ORDER BY v.fechaHoraPartida DESC")
    List<ViajeCompletadoResponse> findViajesCompletadosByConductorId(
            @Param("idConductor") Integer idConductor
    );

}

