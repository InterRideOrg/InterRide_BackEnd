package com.interride.service;

import com.interride.dto.response.NotificacionConductorResponse;
import com.interride.dto.response.NotificacionPasajeroResponse;
import com.interride.dto.response.NotificacionSimpleResponse;

import java.util.List;

public interface NotificacionService {
    boolean enviarNotificacionPasajero(String mensaje, int pasajero_id);
    boolean enviarNotificacionConductor(String mensaje, int conductor_id);

    NotificacionPasajeroResponse crearNotificacionPasajero(String mensaje, int pasajero_id);
    NotificacionConductorResponse crearNotificacionConductor(String mensaje, int conductor_id);

    void eliminarNotificacionesAntiguasPasajero(int pasajeroId);
    void eliminarNotificacionesAntiguasConductor(int conductor_id);

    List<NotificacionPasajeroResponse> obtenerNotificacionesPasajero(int pasajeroId);
    List<NotificacionConductorResponse> obtenerNotificacionesConductor(int conductor_id);


    List<NotificacionSimpleResponse> listarPorPasajero(Integer pasajeroId, String orden);
    List<NotificacionSimpleResponse> listarPorConductor(Integer conductorId, String orden);

    void delete(Integer id);
}
