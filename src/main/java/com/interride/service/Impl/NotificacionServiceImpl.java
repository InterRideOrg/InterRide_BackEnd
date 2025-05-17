package com.interride.service.Impl;

import com.interride.service.NotificacionService;
import com.interride.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificacionServiceImpl implements  NotificacionService  {
    private final NotificacionRepository notificacionRepository;

    public boolean enviarNotificacionPasajero(String mensaje, int pasajero_id) {
        int resultado = notificacionRepository.enviarNotificacionPasajero(mensaje, pasajero_id);
        return resultado > 0;
    }

    public boolean enviarNotificacionConductor(String mensaje, int conductor_id) {
        int resultado = notificacionRepository.enviarNotificacionConductor(mensaje, conductor_id);
        return resultado > 0;
    }
}
