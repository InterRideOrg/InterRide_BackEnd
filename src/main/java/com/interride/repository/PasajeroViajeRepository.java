package com.interride.repository;

import com.interride.model.entity.Calificacion;
import com.interride.model.entity.PasajeroViaje;
import com.interride.model.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PasajeroViajeRepository extends JpaRepository<PasajeroViaje, Integer> {

    @Query("SELECT pv FROM PasajeroViaje pv WHERE pv.viaje.id = :id")
    PasajeroViaje findBoletoInicialIdByViajeId(@Param("id") Integer viajeId);

    @Query("SELECT pv FROM PasajeroViaje pv WHERE pv.viaje.id = :viajeId " +
            "AND pv.estado = 'ACEPTADO'")
    List<PasajeroViaje> findPasajerosAceptadosByViajeId(@Param("viajeId") Integer viajeId);

    @Query("SELECT pv FROM PasajeroViaje pv WHERE pv.viaje.id = :viajeId AND pv.estado = 'COMPLETADO'")
    List<PasajeroViaje> findPasajerosCompletadosByViajeId(@Param("viajeId") Integer viajeId);
}
