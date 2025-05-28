package com.interride.service.Impl;

import com.interride.dto.response.*;

import com.interride.model.enums.EstadoViaje;
import com.interride.repository.NotificacionRepository;
import com.interride.repository.ViajeRepository;
import com.interride.service.ViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ViajeServiceImpl implements ViajeService {

    @Autowired
    private final ViajeRepository viajeRepository;
    @Autowired
    private final NotificacionRepository notificacionRespository;


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
        Boolean viajeCancelado = viajeRepository.isViajeCancelado(idViaje);

        if (viajeCancelado) {
            detalleViajeResponse = obtenerDetalleViajeCancelado(idViaje);
        } else {
            detalleViajeResponse = obtenerDetalleViajeNoCancelado(idViaje, idPasajero);
        }
        return detalleViajeResponse;
    }

    public ViajeEnCursoResponse obtenerDetalleViajeEnCurso(Integer idPasajero) {
        List<Object[]> obj = viajeRepository.getViajeEnCursoById(idPasajero);

        if (obj.isEmpty()) {
            throw new RuntimeException("No hay viajes en curso para el pasajero con id: " + idPasajero);
        }

        Object[] viaje = obj.get(0);

        ViajeEnCursoResponse response = new ViajeEnCursoResponse();
        response.setId((Integer) viaje[0]);
        response.setNombreConductor((String) viaje[1]);
        response.setApellidoConductor((String) viaje[2]);
        response.setModeloVehiculo((String) viaje[3]);
        response.setPlacaVehiculo((String) viaje[4]);
        response.setMarcaVehiculo((String) viaje[5]);
        response.setCantidadAsientos((Integer) viaje[6]);
        response.setAsientosOcupados((Integer) viaje[7]);
        response.setOrigenLongitud(((Number) viaje[8]).doubleValue());
        response.setOrigenLatitud(((Number) viaje[9]).doubleValue());
        response.setOrigenProvincia((String) viaje[10]);
        response.setDestinoLongitud(((Number) viaje[11]).doubleValue());
        response.setDestinoLatitud(((Number) viaje[12]).doubleValue());
        response.setDestinoProvincia((String) viaje[13]);
        response.setEstadoViaje(EstadoViaje.valueOf((String) viaje[14]));
        response.setFecha_hora_partida(((Timestamp) viaje[15]).toLocalDateTime());

        return response;
    }

    @Override
    @Transactional
    public boolean cancelarViaje(Integer idViaje) {
        // Verificar si el viaje existe

        if (!viajeRepository.existsById(idViaje)) {
            throw new RuntimeException("El viaje con ID " + idViaje + " no existe.");
        }

        if (viajeRepository.isViajeCancelado(idViaje)) {
            throw new RuntimeException("El viaje con ID " + idViaje + " ya está cancelado." + viajeRepository.isViajeCancelado(idViaje));
        }
        if (viajeRepository.isViajeEnCurso(idViaje).get(0)[0].equals(true)) {
            throw new RuntimeException("El viaje con ID " + idViaje + " se encuentra en curso y no es cancelable.");
        }


        // Actualizar el estado del viaje a CANCELADO
        int filas_afectadas = viajeRepository.cancelarViaje(idViaje);

        // si se cancelo correctamente, enviar una notificacion a los pasajeros y al conductor
        if (filas_afectadas > 0) {
            String mensaje = "Su viaje con ID " + idViaje + " ha sido cancelado.";
            int conductorId = viajeRepository.getConductorIdByViajeId(idViaje).orElse(-1);
            if (conductorId != -1) {
                notificacionRespository.enviarNotificacionConductor(mensaje, conductorId);
            }
            List<Object[]> pasajerosIds = viajeRepository.getPasajerosIdsByViajeId(idViaje);
            for (Object[] pasajeroId : pasajerosIds) {
                int idPasajero = (Integer) pasajeroId[0];
                notificacionRespository.enviarNotificacionPasajero(mensaje, idPasajero);
            }
        }


        return filas_afectadas>0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViajeDisponibleResponse> obtenerViajesDisponibles(String provinciaOrigen, String provinciaDestino, LocalDate fechaViaje){
        /* Sera descomentado en la fase de produccion
        if(fechaViaje.isBefore(LocalDate.now())){
            throw  new RuntimeException("La fecha del viaje tiene que ser posterior o igual al de hoy");
        }
        */
        return viajeRepository.findViajesDisponibles(
                provinciaOrigen,
                provinciaDestino,
                fechaViaje
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViajeCompletadoResponse> obtenerViajesCompletados(Integer idConductor) {
        return viajeRepository.findViajesCompletadosByConductorId(idConductor);
    }

}

