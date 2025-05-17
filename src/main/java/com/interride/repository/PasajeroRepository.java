package com.interride.repository;

import com.interride.model.entity.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasajeroRepository extends JpaRepository<Pasajero, Integer> {

}
