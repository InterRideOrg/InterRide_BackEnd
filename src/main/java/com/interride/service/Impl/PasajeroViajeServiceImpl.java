package com.interride.service.Impl;

import com.interride.dto.response.BoletoCanceladoResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.model.entity.PasajeroViaje;
import com.interride.model.entity.Ubicacion;
import com.interride.model.entity.Viaje;
import com.interride.model.enums.EstadoViaje;
import com.interride.repository.NotificacionRepository;
import com.interride.repository.PasajeroViajeRepository;
import com.interride.repository.UbicacionRepository;
import com.interride.repository.ViajeRepository;
import com.interride.service.PasajeroViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PasajeroViajeServiceImpl implements PasajeroViajeService {
    private final PasajeroViajeRepository pasajeroViajeRepository;
    private final ViajeRepository viajeRepository;
    private final UbicacionRepository ubicacionRepository;
    private final NotificacionRepository notificacionRepository;
    @Transactional
    @Override
    public BoletoCanceladoResponse ObtenerViajeCanceladoById(Integer id) {
        PasajeroViaje boleto = pasajeroViajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boleto no encontrado con id: " + id));
        Viaje viaje = viajeRepository.findById(boleto.getViaje().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con id: " + boleto.getViaje().getId()));

        Ubicacion origen = ubicacionRepository.findByViajeId(viaje.getId());
        Ubicacion destino = ubicacionRepository.findById(boleto.getUbicacion().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con id: " + boleto.getUbicacion().getId()));

        List<PasajeroViaje> pasajeros = pasajeroViajeRepository.findPasajerosAceptadosByViajeId(viaje.getId());
        //Validaciones

        if(boleto.getEstado().equals(EstadoViaje.ACEPTADO)){
            if(pasajeros.size() == 1){
                viaje.setEstado(EstadoViaje.CANCELADO);
                notificacionRepository.enviarNotificacionConductor(
                        "El viaje con ID " + viaje.getId() + " ha sido cancelado debido a la falta de pasajeros.",
                        viaje.getConductor().getId()
                );
            }

            notificacionRepository.enviarNotificacionPasajero(
                    "El viaje con ID " + viaje.getId() + " ha sido cancelado.",
                    boleto.getPasajero().getId()
            );
            boleto.setEstado(EstadoViaje.CANCELADO);

        }else if(boleto.getEstado().equals(EstadoViaje.SOLICITADO)){
            viaje.setEstado(EstadoViaje.CANCELADO);
            boleto.setEstado(EstadoViaje.CANCELADO);
            notificacionRepository.enviarNotificacionPasajero(
                    "El viaje con ID " + viaje.getId() + " ha sido cancelado.",
                    boleto.getPasajero().getId()
            );
        }else{
            throw new BusinessRuleException("El boleto no se encuentra en un estado válido para ser cancelado.");
        }

        return new BoletoCanceladoResponse(
                boleto.getId(),
                viaje.getId(),
                origen.getProvincia(),
                destino.getProvincia(),
                viaje.getFechaHoraPartida().toString(),
                boleto.getCosto(),
                boleto.getEstado().toString()
        );
    }
}
