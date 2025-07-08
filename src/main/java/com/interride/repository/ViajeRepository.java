package com.interride.repository;

import com.interride.dto.response.ViajeCompletadoResponse;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Viaje;
import com.interride.model.enums.EstadoViaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import com.interride.dto.response.ViajeDisponibleResponse;

import java.util.Optional;


public interface ViajeRepository extends JpaRepository<Viaje, Integer> {
    //getViajesById
    @Query(value = """
        SELECT v.id, v.fecha_hora_partida, v.estado, c.nombres, c.apellidos
        FROM viaje v
        LEFT JOIN conductor c ON v.conductor_id = c.id
        WHERE v.id = :idViaje;
        """, nativeQuery = true)
    Viaje getById(@Param("idViaje") Integer idViaje);

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
        WHERE pv.pasajero_id = :pasajeroid AND v.estado = 'COMPLETADO';
        """, nativeQuery = true)
    List<Object[]> getViajesCompletadosByPasajeroId(@Param("pasajeroid") Integer pasajeroid);

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
        JOIN ubicacion u2 ON u2.pasajero_viaje_id = pv.id
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
    JOIN ubicacion ubi_destino ON ubi_destino.pasajero_viaje_id = pv.id
    WHERE pv.pasajero_id = :idPasajero
      AND pv.estado = 'EN_CURSO';
    """, nativeQuery = true)
    List<Object[]> getViajeEnCursoById(@Param("idPasajero") Integer idPasajero);


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
    FROM viaje v
    JOIN conductor c ON c.id = v.conductor_id
    JOIN vehiculo veh ON veh.conductor_id = c.id
    JOIN ubicacion ubi_origen ON ubi_origen.viaje_id = v.id
    JOIN ubicacion ubi_destino ON ubi_destino.viaje_id = v.id
    WHERE v.conductor_id = :idConductor
      AND v.estado = 'EN_CURSO';
    """, nativeQuery = true)
    List<Object[] > getViajeEnCursoByConductorId(@Param("idConductor") Integer idConductor);

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
    JOIN ubicacion ubi_destino ON ubi_destino.pasajero_viaje_id = pv.id
    WHERE pv.pasajero_id = :idPasajero
      AND v.estado = 'ACEPTADO';
    """, nativeQuery = true)
    List<Object[]> getViajeAceptadoByPasajeroId(@Param("idPasajero") Integer idPasajero);


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
            "JOIN Ubicacion ud ON ud.pasajeroViaje.id = pv.id " + // Destino
            "WHERE v.estado = com.interride.model.enums.EstadoViaje.ACEPTADO " +
            "AND v.asientosDisponibles > 0 ")
    List<ViajeDisponibleResponse> findViajesDisponibles();

    @Query("SELECT NEW com.interride.dto.response.ViajeDisponibleResponse(" +
            "v.id, v.conductor.id,  uo.provincia, ud.provincia, v.fechaHoraPartida, " +
            "uo.direccion, v.asientosDisponibles) " +
            "FROM Viaje v " +
            "JOIN Ubicacion uo ON uo.viaje.id = v.id " + // Origen
            "JOIN PasajeroViaje pv ON pv.viaje.id = v.id " +
            "JOIN Ubicacion ud ON ud.pasajeroViaje.id = pv.id " + // Destino
            "WHERE v.estado = com.interride.model.enums.EstadoViaje.ACEPTADO " +
            "AND v.asientosDisponibles > 0 AND v.id = :viajeId")
    ViajeDisponibleResponse findViajesDisponiblesByViajeId(Integer viajeId);


    @Query("SELECT NEW com.interride.dto.response.ViajeCompletadoResponse(" +
            "v.id, uo.provincia, ud.provincia, uo.direccion, v.fechaHoraPartida, " +
            "SUM(pv.costo), AVG(c.estrellas)) " +
            "FROM Viaje v " +
            "JOIN Ubicacion uo ON uo.viaje.id = v.id " + // Origen
            "JOIN PasajeroViaje pv ON pv.viaje.id = v.id " +
            "JOIN Ubicacion ud ON ud.pasajeroViaje.id = pv.id " + // Destino
            "JOIN Calificacion c ON c.id = v.id " +
            "WHERE v.estado = com.interride.model.enums.EstadoViaje.COMPLETADO " +
            "AND v.conductor.id = :idConductor " +
            "GROUP BY v.id , uo.provincia, ud.provincia, uo.direccion, v.fechaHoraPartida " +
            "ORDER BY v.fechaHoraPartida DESC")
    List<ViajeCompletadoResponse> findViajesCompletadosByConductorId(
            @Param("idConductor") Integer idConductor
    );

    //get PasajeroViaje by viaje id
    @Query(value = """
        SELECT pv
        FROM PasajeroViaje pv
        WHERE pv.viaje.id = :idViaje
    """, nativeQuery = false)
    List<Pasajero> getPasajeroViajesByViajeId(@Param("idViaje") Integer idViaje);


    @Query("SELECT COUNT(pv) " +
            "FROM PasajeroViaje pv " +
            "WHERE pv.viaje.id = :idViaje " +
            "AND pv.estado = com.interride.model.enums.EstadoViaje.EN_CURSO")
    Integer cantidadBoletosEnCursoPorViaje(@Param("idViaje") Integer idViaje);

    //Obtener viajes con estado X retornando List<Viaje>
    @Query("SELECT v FROM Viaje v WHERE v.estado = :estado")
    List<Viaje> findByEstado(@Param("estado") EstadoViaje estado);

    @Query("SELECT v FROM Viaje v WHERE v.conductor.id = :conductorId AND v.estado = :estado")
    List<Viaje> findByConductorIdAndEstado(
            @Param("conductorId") Integer conductorId,
            @Param("estado") EstadoViaje estado
    );

    @Query("SELECT v FROM Viaje v WHERE v.conductor.id = :conductorId AND v.estado = :estado")
    List<Viaje> findByConductorIdAndState(
            @Param("conductorId") Integer conductorId,
            @Param("estado") EstadoViaje estado
    );

}

