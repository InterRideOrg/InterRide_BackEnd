package com.interride.service;

import com.interride.model.entity.Calificacion;

import java.util.List;
import java.util.Optional;

public interface CalificacionService {
    List<Calificacion> getAll();
    List<Calificacion> findByViajeId(Integer viajeId);
    List<Calificacion> findByConductorId(Integer conductorId);

    Calificacion findById(Integer id);
    Calificacion create(Calificacion calificacion);
    Calificacion update(Integer id, Calificacion calificacion);
    void delete(Integer id);

}
