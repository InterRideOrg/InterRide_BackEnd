package com.interride.repository;

import com.interride.model.entity.PasajeroViaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasajeroViajeRepository extends JpaRepository<PasajeroViaje, Long> {
    Optional<PasajeroViaje> findByIdAndPasajeroId(Integer pasajeroViajeId, Integer pasajeroId);
}
