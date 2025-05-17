package com.interride.mapper;

import com.interride.dto.request.CreateCalificacionRequest;
import com.interride.dto.response.CalificacionResponse;
import com.interride.model.entity.Calificacion;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Viaje;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CalificacionMapper {

    public Calificacion toEntity(CreateCalificacionRequest request) {
        if (request == null) {
            return null;
        }
        return Calificacion.builder()
                .estrellas(request.estrellas())
                .comentario(request.comentario())
                .pasajero(
                        Pasajero.builder()
                                .id(request.pasajeroId())
                                .build())
                .viaje(
                        Viaje.builder()
                                .id(request.pasajeroId())
                                .build()
                )
                .conductor(
                        Conductor.builder()
                                .id(request.conductorId())
                                .build()
                )
                .build();
    }

    public CalificacionResponse toResponse(Calificacion calificacion) {
        if (calificacion == null) {
            return null;
        }
        return new CalificacionResponse(
                calificacion.getId(),
                calificacion.getEstrellas(),
                calificacion.getComentario(),
                calificacion.getViaje().getId(),
                calificacion.getConductor().getId(),
                calificacion.getPasajero().getId()
        );
    }

}