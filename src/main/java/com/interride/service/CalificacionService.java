package com.interride.service;

import com.interride.dto.response.CalificacionPromedioConductorResponse;
import com.interride.dto.response.CalificacionResponse;
import com.interride.dto.request.CreateCalificacionRequest;
import com.interride.dto.request.UpdateCalificacionRequest;
import com.interride.model.entity.Calificacion;

import java.util.List;

public interface CalificacionService {
    List<CalificacionResponse> getAll();
    List<CalificacionResponse> findByViajeId(Integer viajeId);
    CalificacionResponse getByPasajeroIdAndViajeId(Integer pasajeroId, Integer viajeId);
    //List<CalificacionResponse> findByConductorId(Integer conductorId);

    CalificacionResponse create(CreateCalificacionRequest request);
    CalificacionResponse update(Integer id, UpdateCalificacionRequest request);
    void delete(Integer id);

    CalificacionPromedioConductorResponse findAverageRatingAndCommentsByConductorId(Integer conductorId);
}
