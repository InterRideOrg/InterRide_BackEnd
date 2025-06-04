package com.interride.mapper;

import com.interride.dto.response.NotificacionConductorResponse;
import com.interride.dto.response.NotificacionPasajeroResponse;
import com.interride.model.entity.Notificacion;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {

    public NotificacionPasajeroResponse toNotificacionPasajeroResponse(Notificacion notificacion) {
        if (notificacion == null) {
            return null;
        }
        return new NotificacionPasajeroResponse(
                notificacion.getId(),
                notificacion.getMensaje(),
                notificacion.getFechaHoraEnvio().toString(),
                notificacion.getLeido(),
                notificacion.getPasajero().getId()
        );
    }

    public NotificacionConductorResponse toNotificacionConductorResponse(Notificacion notificacion) {
        if (notificacion == null) {
            return null;
        }
        return new NotificacionConductorResponse(
                notificacion.getId(),
                notificacion.getMensaje(),
                notificacion.getFechaHoraEnvio().toString(),
                notificacion.getLeido(),
                notificacion.getConductor().getId()
        );
    }
}
