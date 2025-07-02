package com.interride.repository;

import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Integer> {

    @Query("SELECT t FROM Tarjeta t WHERE t.pasajero.id = :pasajeroId")
    List<Tarjeta> findByPasajeroId(@Param("pasajeroId") Integer pasajeroId);

    @Query("SELECT t FROM Tarjeta t WHERE t.conductor.id = :conductorId")
    Tarjeta findByConductorId(Integer conductorId);

    Boolean existsTarjetaByNumeroTarjeta(String numeroTarjeta);
}
