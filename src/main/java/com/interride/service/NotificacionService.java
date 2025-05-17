package com.interride.service;

public interface NotificacionService {
    boolean enviarNotificacionPasajero(String mensaje, int pasajero_id);
    boolean enviarNotificacionConductor(String mensaje, int conductor_id);
}
