package com.interride.repository;

import com.interride.model.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO notificacion (mensaje, pasajero_id, leido, fecha_hora_envio)
        VALUES (:mensaje, :pasajero_id, false, NOW())
        """, nativeQuery = true)
    int enviarNotificacionPasajero(@Param("mensaje") String mensaje, @Param("pasajero_id") Integer pasajero_id);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO notificacion (mensaje, conductor_id, leido, fecha_hora_envio)
        VALUES (:mensaje, :conductor_id, false, NOW())
        """, nativeQuery = true)
    int enviarNotificacionConductor(@Param("mensaje") String mensaje, @Param("conductor_id") Integer conductor_id);

    List<Notificacion> findByConductorId(Integer conductorId);
    List<Notificacion> findByPasajeroId(Integer pasajeroId);

    void deleteByPasajeroIdAndFechaHoraEnvioBefore(Integer pasajeroId, LocalDateTime limite);
    void deleteByConductorIdAndFechaHoraEnvioBefore(Integer conductorId, LocalDateTime  limite);
}
