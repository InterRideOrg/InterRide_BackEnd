package com.interride.repository;

import com.interride.model.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    @Query(value="SELECT p FROM Pago p WHERE p.pasajero.id = :id")
    List<Pago> findByPasajeroId(@Param("id") Integer id);
    List<Pago> findByConductorId(Integer conductorId);
}
