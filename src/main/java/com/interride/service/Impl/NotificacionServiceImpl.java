package com.interride.service.Impl;

import com.interride.dto.response.NotificacionConductorResponse;
import com.interride.dto.response.NotificacionPasajeroResponse;
import com.interride.dto.response.NotificacionSimpleResponse;
import com.interride.exception.ResourceNotFoundException;
import com.interride.exception.ValidationException;
import com.interride.mapper.NotificacionMapper;

import com.interride.model.entity.Conductor;
import com.interride.model.entity.Notificacion;
import com.interride.model.entity.Pasajero;
import com.interride.repository.ConductorRepository;
import com.interride.repository.PasajeroRepository;
import com.interride.service.NotificacionService;
import com.interride.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificacionServiceImpl implements  NotificacionService  {
    private final NotificacionRepository notificacionRepository;
    private final PasajeroRepository pasajeroRepository;
    private final ConductorRepository conductorRepository;

    private final NotificacionMapper notificacionMapper;

    public boolean enviarNotificacionPasajero(String mensaje, int pasajero_id) {
        int resultado = notificacionRepository.enviarNotificacionPasajero(mensaje, pasajero_id);
        return resultado > 0;
    }

    public boolean enviarNotificacionConductor(String mensaje, int conductor_id) {
        int resultado = notificacionRepository.enviarNotificacionConductor(mensaje, conductor_id);
        return resultado > 0;
    }

    @Override
    @Transactional
    public NotificacionPasajeroResponse crearNotificacionPasajero(String mensaje, int pasajero_id) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new ValidationException("El mensaje no puede estar vacío.");
        }

        Pasajero pasajero = pasajeroRepository.findById(pasajero_id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con ID: " + pasajero_id));

        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(mensaje);
        notificacion.setFechaHoraEnvio(LocalDateTime.now());
        notificacion.setLeido(Boolean.FALSE);
        notificacion.setPasajero(pasajero);

        Notificacion nuevaNotificacion = notificacionRepository.save(notificacion);
        return new NotificacionPasajeroResponse(
                nuevaNotificacion.getId(),
                nuevaNotificacion.getMensaje(),
                nuevaNotificacion.getFechaHoraEnvio().toString(),
                nuevaNotificacion.getLeido(),
                nuevaNotificacion.getPasajero().getId()
        );
    }

    @Override
    @Transactional
    public NotificacionConductorResponse crearNotificacionConductor(String mensaje, int conductor_id) {
        if(mensaje == null || mensaje.trim().isEmpty()) {
            throw new ValidationException("El mensaje no puede estar vacío.");
        }

        var conductor = conductorRepository.findById(conductor_id)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con ID: " + conductor_id));

        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(mensaje);
        notificacion.setFechaHoraEnvio(LocalDateTime.now());
        notificacion.setLeido(Boolean.FALSE);
        notificacion.setConductor(conductor);
        Notificacion nuevaNotificacion = notificacionRepository.save(notificacion);
        return new NotificacionConductorResponse(
                nuevaNotificacion.getId(),
                nuevaNotificacion.getMensaje(),
                nuevaNotificacion.getFechaHoraEnvio().toString(),
                nuevaNotificacion.getLeido(),
                nuevaNotificacion.getConductor().getId()
        );
    }

    @Override
    @Transactional
    public void eliminarNotificacionesAntiguasPasajero(int pasajeroId){
        Pasajero pasajero = pasajeroRepository.findById(pasajeroId)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con ID: " + pasajeroId));
        LocalDateTime hace21Dias = LocalDateTime.now().minusDays(21);
        notificacionRepository.deleteByPasajeroIdAndFechaHoraEnvioBefore(pasajero.getId(), hace21Dias);
    }

    @Override
    @Transactional
    public void eliminarNotificacionesAntiguasConductor(int conductorId){
        Conductor conductor = conductorRepository.findById(conductorId)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con ID: " + conductorId));
        LocalDateTime hace21Dias = LocalDateTime.now().minusDays(21);
        notificacionRepository.deleteByConductorIdAndFechaHoraEnvioBefore(conductor.getId(), hace21Dias);
    }

    @Override
    @Transactional
    public List<NotificacionPasajeroResponse> obtenerNotificacionesPasajero(int pasajeroId){
        Pasajero pasajero = pasajeroRepository.findById(pasajeroId)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con ID: " + pasajeroId));
        List<Notificacion> notificaciones = notificacionRepository.findByPasajeroId(pasajero.getId());
        return notificaciones.stream()
                .map(notificacionMapper::toNotificacionPasajeroResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<NotificacionConductorResponse> obtenerNotificacionesConductor(int conductorId){
        Conductor conductor = conductorRepository.findById(conductorId)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con ID: " + conductorId));
        List<Notificacion> notificaciones = notificacionRepository.findByConductorId(conductor.getId());
        return notificaciones.stream()
                .map(notificacionMapper::toNotificacionConductorResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Integer id){
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + id));
        notificacionRepository.delete(notificacion);
    }


    @Override
    public List<NotificacionSimpleResponse> listarPorPasajero(Integer pasajeroId, String orden) {
        Sort sort = orden.equalsIgnoreCase("asc") ? Sort.by("fechaHoraEnvio").ascending()
                : Sort.by("fechaHoraEnvio").descending();

        List<Notificacion> notificaciones = notificacionRepository.findAllByPasajeroId(pasajeroId, sort);

        return notificaciones.stream()
                .map(notificacionMapper::toNotificacionSimpleResponse)
                .toList();
    }

    @Override
    public List<NotificacionSimpleResponse> listarPorConductor(Integer conductorId, String orden) {
        Sort sort = orden.equalsIgnoreCase("asc") ? Sort.by("fechaHoraEnvio").ascending()
                : Sort.by("fechaHoraEnvio").descending();

        List<Notificacion> notificaciones = notificacionRepository.findAllByConductorId(conductorId, sort);

        return notificaciones.stream()
                .map(notificacionMapper::toNotificacionSimpleResponse)
                .toList();
    }


}
