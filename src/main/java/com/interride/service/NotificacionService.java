package com.interride.service;

import com.interride.dto.response.NotificacionConductorResponse;
import com.interride.dto.response.NotificacionPasajeroResponse;

public interface NotificacionService {
    boolean enviarNotificacionPasajero(String mensaje, int pasajero_id);
    boolean enviarNotificacionConductor(String mensaje, int conductor_id);

    NotificacionPasajeroResponse crearNotificacionPasajero(String mensaje, int pasajero_id);
    NotificacionConductorResponse crearNotificacionConductor(String mensaje, int conductor_id);

    void delete(Integer id);
}
