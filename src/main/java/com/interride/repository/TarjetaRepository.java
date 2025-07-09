package com.interride.repository;

import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Integer> {

}
