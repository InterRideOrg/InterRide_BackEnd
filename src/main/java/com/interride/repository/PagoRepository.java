package com.interride.repository;

import com.interride.model.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    @Query(value="SELECT p FROM Pago p WHERE p.pasajero.id = :id")
    List<Pago> findByPasajeroId(@Param("id") Integer id);

    List<Pago> findByConductorId(@Param("id") Integer conductorId);


    //Duelve los pagos realizados en un año determinado divididos por mes
    @Query(value = "SELECT EXTRACT(MONTH FROM p.fecha_hora_pago) AS mes, SUM(p.monto) AS total " +
            "FROM Pago p " +
            "WHERE EXTRACT(YEAR FROM p.fecha_hora_pago) = :year " +
            "AND p.conductor_id = :conductorId " +
            "GROUP BY mes ORDER BY mes", nativeQuery = true)
    List<Object[]> findPagosByYearGroupedByMonth(@Param("year") Integer year,
                                                  @Param("conductorId") Integer conductorId);

    //Duelve los pagos realizados en un mes dividos por día
    @Query(value = "SELECT EXTRACT(DAY FROM p.fecha_hora_pago) AS dia, SUM(p.monto) AS total " +
            "FROM Pago p " +
            "WHERE EXTRACT(YEAR FROM p.fecha_hora_pago) = :year " +
            "AND EXTRACT(MONTH FROM p.fecha_hora_pago) = :month " +
            "AND p.conductor_id = :conductorId " +
            "GROUP BY dia ORDER BY dia", nativeQuery = true)
    List<Object[]> findPagosByMonthGroupedByDay(@Param("year") Integer year,
                                                  @Param("month") Integer month,
                                                  @Param("conductorId") Integer conductorId);


}
