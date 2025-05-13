package com.interride.service.Impl;

import com.interride.model.entity.Calificacion;
import com.interride.model.entity.Conductor;
import com.interride.repository.CalificacionRepository;
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
    private final CalificacionRepository calificacionRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Calificacion> getAll() {
        return calificacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Calificacion> findByViajeId(Integer viajeId) {
        return calificacionRepository.findByViajeId(viajeId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Calificacion> findByConductorId(Integer conductorId) {
        return calificacionRepository.findByConductorId(conductorId);
    }

    @Transactional(readOnly = true)
    @Override
    public Calificacion findById(Integer id) {
        return calificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calificacion no encontrada"));
    }

    @Transactional
    @Override
    public Calificacion create(Calificacion calificacion) {
        return calificacionRepository.save(calificacion);
    }

    @Transactional
    @Override
    public Calificacion update(Integer id, Calificacion nueva_calificacion) {
        Calificacion calificacionActual = findById(id);
        calificacionActual.setEstrellas(nueva_calificacion.getEstrellas());
        calificacionActual.setComentario(nueva_calificacion.getComentario());
        return calificacionRepository.save(calificacionActual);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Calificacion calificacion = findById(id);
        calificacionRepository.delete(calificacion);
    }


}
