package com.interride.service.Impl;

import com.interride.dto.response.CalificacionPromedioConductorResponse;
import com.interride.dto.response.CalificacionResponse;
import com.interride.dto.request.CreateCalificacionRequest;
import com.interride.dto.request.UpdateCalificacionRequest;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.model.entity.Calificacion;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Viaje;
import com.interride.repository.CalificacionRepository;
import com.interride.repository.ConductorRepository;
import com.interride.repository.PasajeroRepository;
import com.interride.repository.ViajeRepository;
import com.interride.mapper.CalificacionMapper;
import com.interride.service.CalificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CalificacionServiceImpl implements CalificacionService {
    private final CalificacionMapper calificacionMapper;

    private final CalificacionRepository calificacionRepository;
    private final ConductorRepository conductorRepository;
    private final ViajeRepository viajeRepository;
    private final PasajeroRepository pasajeroRepository;

    private Calificacion findById(Integer id) {
        return calificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calificacion no encontrada con id:" + id));
    }

    private Viaje findViajeById(Integer id) {
        return viajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Viaje no encontrado con id: " + id));
    }

    private Conductor findConductorById(Integer id) {
        return conductorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conductor no encontrado con id: " + id));
    }

    private Pasajero findPasajeroById(Integer id) {
        return pasajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pasajero no encontrado con id: " + id));
    }

    private void validateCalificacion(Calificacion calificacion) {
        if (calificacion.getEstrellas() < 1 || calificacion.getEstrellas() > 5) {
            throw new BusinessRuleException("El número de estrellas debe estar entre 1 y 5");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<CalificacionResponse> findByConductorId(Integer conductorId) {
        List<Calificacion> calificacionsPorConductor = calificacionRepository.findByConductorId(conductorId);
        return calificacionsPorConductor.stream().map(calificacionMapper::toResponse).toList();
    }


    // Funcionalidades finales

    @Transactional(readOnly = true)
    @Override
    public List<CalificacionResponse> getAll() {
        List<Calificacion> calificaciones = calificacionRepository.findAll();
        return calificaciones.stream().map(calificacionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CalificacionResponse> findByViajeId(Integer viajeId) {
        List<Calificacion> calificacionsPorViaje = calificacionRepository.findByViajeId(viajeId);
        return calificacionsPorViaje.stream().map(calificacionMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CalificacionPromedioConductorResponse findAverageRatingAndCommentsByConductorId(Integer conductorId) {
        Double promedioCalificacion = calificacionRepository.findAverageRatingByConductorId(conductorId);
        List<CalificacionResponse> calificaciones = calificacionRepository.findByConductorId(conductorId)
                .stream().map(calificacionMapper::toResponse).toList();

        if (calificaciones.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron calificaciones para el conductor con id: " + conductorId);
        }

        return new CalificacionPromedioConductorResponse(
                conductorId,
                promedioCalificacion,
                calificaciones
        );
    }

    @Transactional
    @Override
    public CalificacionResponse create(CreateCalificacionRequest request) {
        Calificacion calificacion = calificacionMapper.toEntity(request);

        calificacion.setViaje(findViajeById(calificacion.getViaje().getId()));
        calificacion.setConductor(findConductorById(calificacion.getConductor().getId()));
        calificacion.setPasajero(findPasajeroById(calificacion.getPasajero().getId()));

        validateCalificacion(calificacion);

        Calificacion nuevaCalificacion = calificacionRepository.save(calificacion);

        return calificacionMapper.toResponse(nuevaCalificacion);
    }

    @Transactional
    @Override
    public CalificacionResponse update(Integer id, UpdateCalificacionRequest request) {
        if (request.estrellas() < 1 || request.estrellas() > 5) {
            throw new BusinessRuleException("El número de estrellas debe estar entre 1 y 5");
        }

        Calificacion calificacionActual = calificacionRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Calificacion con id " + id + " no encontrado" ));

        calificacionActual.setEstrellas(request.estrellas());
        calificacionActual.setComentario(request.comentario());

        Calificacion calificacionActualizado = calificacionRepository.save(calificacionActual);
        return calificacionMapper.toResponse(calificacionActualizado);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Calificacion con id " + id + " no encontrado" ));
        calificacionRepository.delete(calificacion);
    }


}
