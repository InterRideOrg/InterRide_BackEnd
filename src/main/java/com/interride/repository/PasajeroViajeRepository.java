package com.interride.repository;

import com.interride.model.entity.PasajeroViaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PasajeroViajeRepository extends JpaRepository<PasajeroViaje, Integer> {

    @Query("SELECT pv FROM PasajeroViaje pv WHERE pv.viaje.id = :id")
    PasajeroViaje findBoletoInicialIdByViajeId(@Param("id") Integer viajeId);
}
