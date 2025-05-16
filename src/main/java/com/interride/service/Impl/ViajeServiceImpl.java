package com.interride.service.Impl;

import com.interride.dto.response.PasajeroViajesResponse;
import com.interride.model.enums.EstadoViaje;
import com.interride.repository.ViajeRepository;
import com.interride.service.ViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.interride.dto.response.DetalleViajeResponse;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ViajeServiceImpl implements ViajeService {
    private final ViajeRepository viajeRepository;

    @Override
    public List<PasajeroViajesResponse> getViajesByPasajeroId(Integer pasajeroId) {
        List<Object[]> resultados = viajeRepository.getViajesByPasajeroId(pasajeroId);
        // Verificar si se encontraron resultados
        if (resultados.isEmpty()) {
            throw new RuntimeException("No se encontraron viajes para el pasajero con id: " + pasajeroId);
        }
        return resultados.stream().map(obj -> new PasajeroViajesResponse(
                (Integer) obj[0],
                ((Timestamp) obj[1]).toLocalDateTime(), // Convertir
                EstadoViaje.valueOf((String) obj[2]),    // Convertir string a enum
                (String) obj[3],
                (String) obj[4],
                ((Timestamp) obj[5]).toLocalDateTime(), // Convertir
                ((Timestamp) obj[6]).toLocalDateTime(), // Convertir
                ((Number) obj[7]).doubleValue()
        )).toList();
    }

    public DetalleViajeResponse obtenerDetalleViajeNoCancelado(Integer idViaje, Integer idPasajero) {
        List<Object[]> obj = viajeRepository.getDetalleViajeById(idViaje, idPasajero);

        if (obj.isEmpty()) {
            throw new RuntimeException("Viaje no encontrado.");
        }

        Object[] viaje = obj.get(0);

        DetalleViajeResponse response = new DetalleViajeResponse();
        response.setFechaHora(((Timestamp) viaje[0]).toLocalDateTime());
        response.setOrigen((String) viaje[1]);
        response.setDestino((String) viaje[2]);
        response.setConductorNombreCompleto((String) viaje[3]); // puede ser null
        response.setModoPago((String) viaje[4]); // puede ser null
        response.setMontoPagado(((Number) viaje[5]).doubleValue()); // puede ser null
        response.setCalificacionEstrellas((Integer) viaje[6]); // puede ser null
        response.setEstado(EstadoViaje.valueOf((String) viaje[7])); // siempre será "FINALIZADO" o "EN CURSO"
        return response;
    }

    public DetalleViajeResponse obtenerDetalleViajeCancelado(Integer idViaje) {
        List<Object[]> obj = viajeRepository.getDetalleViajeCancelado(idViaje);

        if (obj.isEmpty()) {
            throw new RuntimeException("Viaje no encontrado.");
        }
        Object[] viaje = obj.get(0);

        DetalleViajeResponse response = new DetalleViajeResponse();
        response.setFechaHora(((Timestamp) viaje[0]).toLocalDateTime());
        response.setOrigen((String) viaje[1]);
        response.setDestino((String) viaje[2]);
        response.setConductorNombreCompleto((String) viaje[3]); // puede ser null
        response.setModoPago(null); // no se aplica
        response.setMontoPagado(null); // no se aplica
        response.setCalificacionEstrellas(null); // no se aplica
        response.setEstado(EstadoViaje.CANCELADO); // siempre será "CANCELADO"
        return response;
    }

    public DetalleViajeResponse obtenerDetalleViaje(Integer idViaje, Integer idPasajero) {
        DetalleViajeResponse detalleViajeResponse;

        // Verificar si el viaje está cancelado
        boolean viajeCancelado = viajeRepository.isViajeCancelado(idViaje);

        if (viajeCancelado) {
            detalleViajeResponse = obtenerDetalleViajeCancelado(idViaje);
        } else {
            detalleViajeResponse = obtenerDetalleViajeNoCancelado(idViaje, idPasajero);
        }
        return detalleViajeResponse;
    }

}