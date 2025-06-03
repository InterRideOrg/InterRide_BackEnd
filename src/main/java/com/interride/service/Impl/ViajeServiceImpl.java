package com.interride.service.Impl;

import com.interride.dto.response.*;

import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.ViajeMapper;
import com.interride.model.entity.*;
import com.interride.model.enums.EstadoViaje;
import com.interride.repository.*;
import com.interride.service.ViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ViajeServiceImpl implements ViajeService {
    private final ViajeRepository viajeRepository;
    private final NotificacionRepository notificacionRepository;
    private final ConductorRepository conductorRepository;
    private final PasajeroViajeRepository pasajeroViajeRepository;
    private final UbicacionRepository ubicacionRepository;

    private final ViajeMapper viajeMapper;

    @Override
    public List<PasajeroViajesResponse> getViajesByPasajeroId(Integer pasajeroId) {
        List<Object[]> resultados = viajeRepository.getViajesByPasajeroId(pasajeroId);
        // Verificar si se encontraron resultados
        if (resultados.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron viajes para el pasajero con id: " + pasajeroId);
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
            throw new ResourceNotFoundException("Viaje no encontrado.");
        }

        Object[] viaje = obj.getFirst();

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
            throw new ResourceNotFoundException("Viaje no encontrado.");
        }
        Object[] viaje = obj.getFirst();

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
            throw new ResourceNotFoundException("No hay viajes en curso para el pasajero con id: " + idPasajero);
        }

        Object[] viaje = obj.getFirst();

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
    public ViajeCanceladoResponse cancelarViaje(Integer idViaje) {
        // Verificar si el viaje existe
        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new ResourceNotFoundException("El viaje con ID " + idViaje + " no existe."));
        Conductor conductor = conductorRepository.findById(viaje.getConductor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("El conductor con ID " + viaje.getConductor().getId() + " no existe."));

        List<PasajeroViaje> boletos = pasajeroViajeRepository.findPasajerosAceptadosByViajeId(viaje.getId());

        if(!viaje.getEstado().equals(EstadoViaje.ACEPTADO)){
            throw  new BusinessRuleException("El viaje con ID " + idViaje + " no está en estado ACEPTADO y no puede ser cancelado.");
        }

        Ubicacion origen = ubicacionRepository.findByViajeId(viaje.getId());

        Ubicacion destino = ubicacionRepository.findByPasajeroViajeId(boletos.getFirst().getId());
        //Actualizar el estado del viaje y de los boletos de pasajeros
        viaje.setEstado(EstadoViaje.CANCELADO);

        for(PasajeroViaje boleto : boletos){
            boleto.setEstado(EstadoViaje.CANCELADO);
            pasajeroViajeRepository.save(boleto);
        }

        // Enviar notificación al conductor y a los pasajeros
        notificacionRepository.enviarNotificacionConductor(
                "El viaje con ID " + idViaje + " ha sido cancelado por el conductor.",
                conductor.getId()
        );

        for(PasajeroViaje boleto : boletos){
            notificacionRepository.enviarNotificacionPasajero(
                    "El viaje con ID " + idViaje + " ha sido cancelado por el conductor. ¿Desea solicitar el mismo viaje?",
                    boleto.getPasajero().getId()
            );
        }

        // Guardar el viaje actualizado
        Viaje viajeCancelado = viajeRepository.save(viaje);

        return new ViajeCanceladoResponse(
                viajeCancelado.getId(),
                viajeCancelado.getEstado(),
                origen.getProvincia(),
                destino.getProvincia(),
                viajeCancelado.getFechaHoraPartida(),
                LocalDateTime.now()
        );
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

    @Override
    @Transactional
    public ViajeAceptadoResponse aceptarViaje(Integer idViaje, Integer idConductor) {
        // Verificar si el viaje existe
        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new ResourceNotFoundException("El viaje con ID " + idViaje + " no existe."));

        // Verificar si el conductor existe
        Conductor conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new ResourceNotFoundException("El conductor con ID " + idConductor + " no existe."));

        // Obtener el ID de la ubicación de destino del viaje
        PasajeroViaje boletoInicial = pasajeroViajeRepository.findBoletoInicialIdByViajeId(viaje.getId());



        Ubicacion origen = ubicacionRepository.findByViajeId(viaje.getId());

        Ubicacion destino = ubicacionRepository.findByPasajeroViajeId(boletoInicial.getId());
        // Verificar si el viaje ya está aceptado
        if (!viaje.getEstado().equals(EstadoViaje.SOLICITADO)) {
            throw new BusinessRuleException("Solo se puede aceptar un viaje con estado 'SOLICITADO'. El viaje con ID " + idViaje + " tiene estado: " + viaje.getEstado());
        }

        // Verificar si el conductor tiene un vehiculo
        if (conductor.getVehiculo() == null) {
            throw new BusinessRuleException("El conductor con ID " + idConductor + " no tiene un vehículo asociado.");
        }

        //Verificar si el carro del conductor tiene asientos suficientes
        if(viaje.getAsientosOcupados() > conductor.getVehiculo().getCantidadAsientos()){
            throw new BusinessRuleException("El carro del conductor con ID " + idConductor + " no tiene asientos suficientes para aceptar el viaje.");
        }

        //Actualizar el boleto inicial
        boletoInicial.setEstado(EstadoViaje.ACEPTADO);
        pasajeroViajeRepository.save(boletoInicial);


        // Actualizar el estado del viaje a ACEPTADO
        viaje.setEstado(EstadoViaje.ACEPTADO);
        viaje.setAsientosDisponibles(conductor.getVehiculo().getCantidadAsientos() - viaje.getAsientosOcupados());
        viaje.setConductor(conductor);

        Viaje viajeAceptado = viajeRepository.save(viaje);

        return viajeMapper.toViajeAceptadoResponse(viajeAceptado, conductor, origen, destino);
    }

    @Override
    @Transactional
    public boolean empezarViaje(Integer idViaje, Integer idConductor) {

        // Verificar si el viaje existe
        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new ResourceNotFoundException("El viaje con ID " + idViaje + " no existe."));

        // Verificar si es la hora de inicio del viaje con margen de 30 minutos
        LocalDateTime horaInicioViaje = viaje.getFechaHoraPartida();
        LocalDateTime horaActual = LocalDateTime.now();
        if (horaActual.isBefore(horaInicioViaje.minusMinutes(30)) || horaActual.isAfter(horaInicioViaje.plusMinutes(30))) {
            throw new BusinessRuleException("El viaje con ID " + idViaje + " no puede comenzar ahora. La hora de inicio es: " + horaInicioViaje);
        }

        // Verificar si el conductor existe
        Conductor conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new ResourceNotFoundException("El conductor con ID " + idConductor + " no existe."));

        // Verificar si el viaje está en estado ACEPTADO
        if (!viaje.getEstado().equals(EstadoViaje.ACEPTADO)) {
            throw new BusinessRuleException("Solo se puede empezar un viaje con estado 'ACEPTADO'. El viaje con ID " + idViaje + " tiene estado: " + viaje.getEstado());
        }

        // Verificar que los pasajeros estan estan abordo (PasajerViaje.abordo == true)
        List<PasajeroViaje> boletos = pasajeroViajeRepository.findPasajerosAceptadosByViajeId(viaje.getId());
        if (boletos.isEmpty()) {
            throw new BusinessRuleException("No hay pasajeros aceptados para el viaje con ID " + idViaje + ".");
        }
        for (PasajeroViaje boleto : boletos) {
            if (!boleto.getAbordo()) {
                throw new BusinessRuleException("El pasajero con ID " + boleto.getPasajero().getId() + " no está a bordo del viaje con ID " + idViaje + ".");
            }
        }

        // Actualizar el estado del viaje a EN CURSO
        viaje.setEstado(EstadoViaje.EN_CURSO);
        viaje.setFechaHoraPartida(LocalDateTime.now());

        Viaje viajeEnCurso = viajeRepository.save(viaje);

        Ubicacion origen = ubicacionRepository.findByViajeId(viaje.getId());
        PasajeroViaje boletoInicial = pasajeroViajeRepository.findBoletoInicialIdByViajeId(viaje.getId());
        Ubicacion destino = ubicacionRepository.findByPasajeroViajeId(boletoInicial.getId());

        // Enviar notificación al conductor
        notificacionRepository.enviarNotificacionConductor(
                "El viaje con ID " + idViaje + " ha comenzado.",
                conductor.getId()
        );
        // Enviar notificación a los pasajeros
        for (PasajeroViaje boleto : boletos) {
            notificacionRepository.enviarNotificacionPasajero(
                    "El viaje con ID " + idViaje + " ha comenzado.",
                    boleto.getPasajero().getId()
            );
        }

        // Crear la respuesta
        return true;
    }
}

