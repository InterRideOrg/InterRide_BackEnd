package com.interride.service.Impl;

import com.interride.dto.request.ViajeSolicitadoRequest;
import com.interride.dto.response.*;

import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.integration.notification.email.dto.Mail;
import com.interride.integration.notification.email.service.EmailService;
import com.interride.mapper.PasajeroViajeMapper;
import com.interride.mapper.UbicacionMapper;
import com.interride.mapper.ViajeMapper;
import com.interride.model.entity.*;
import com.interride.model.enums.EstadoPago;
import com.interride.model.enums.EstadoViaje;
import com.interride.repository.*;
import com.interride.service.ViajeService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ViajeServiceImpl implements ViajeService {
    private final ViajeRepository viajeRepository;
    private final NotificacionRepository notificacionRepository;
    private final ConductorRepository conductorRepository;
    private final PasajeroViajeRepository pasajeroViajeRepository;
    private final UbicacionRepository ubicacionRepository;
    private final PasajeroRepository pasajeroRepository;
    private final CalificacionRepository calificacionRepository;
    private final PagoRepository pagoRepository;
    private final EmailService emailService;

    private final ViajeMapper viajeMapper;
    private final UbicacionMapper ubicacionMapper;
    private final PasajeroViajeMapper pasajeroViajeMapper;

    private String mailFrom = "interrideorg@gmail.com";

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
    public List<PasajeroViajesResponse> getViajesCompletadosByPasajeroId(Integer pasajeroId){
        List<Object[]> resultados = viajeRepository.getViajesCompletadosByPasajeroId(pasajeroId);
        // Verificar si se encontraron resultados
        if (resultados.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron viajes completados para el pasajero con id: " + pasajeroId);
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

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
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
        if(viaje.getConductor() == null){
            throw new BusinessRuleException("El viaje con ID " + idViaje + " no tiene un conductor asignado.");
        }

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
    public List<ViajeDisponibleResponse> obtenerViajesDisponibles() {
        return viajeRepository.findViajesDisponibles();
    }

    @Override
    @Transactional(readOnly = true)
    public ViajeDisponibleResponse obtenerViajesDisponiblesByViajeId(Integer viajeId) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new ResourceNotFoundException("El viaje con ID " + viajeId + " no existe."));

        if(viaje.getEstado() != EstadoViaje.ACEPTADO) {
            throw new BusinessRuleException("El viaje con ID " + viajeId + " no está en estado ACEPTADO y no puede ser consultado.");
        }

        return viajeRepository.findViajesDisponiblesByViajeId(viaje.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViajeCompletadoResponse> obtenerViajesCompletados(Integer idConductor) {
        Conductor conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new ResourceNotFoundException("El conductor con ID " + idConductor + " no existe."));

        return viajeRepository.findViajesCompletadosByConductorId(conductor.getId());
    }

    @Override
    @Transactional
    public ViajeAceptadoResponse aceptarViaje(Integer idViaje, Integer idConductor) throws Exception {
        // Verificar si el viaje existe
        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new ResourceNotFoundException("El viaje con ID " + idViaje + " no existe."));

        // Verificar si el conductor existe
        Conductor conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new ResourceNotFoundException("El conductor con ID " + idConductor + " no existe."));

        // Obtener el ID de la ubicación de destino del viaje
        PasajeroViaje boletoInicial = pasajeroViajeRepository.findBoletoInicialIdByViajeId(viaje.getId());

        Pasajero pasajero = pasajeroRepository.findById(boletoInicial.getPasajero().getId())
                .orElseThrow(() -> new ResourceNotFoundException("El pasajero con ID " + boletoInicial.getPasajero().getId() + " no existe."));

        Usuario usuarioPasajero = pasajero.getUsuario();

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

        //Enviar notificacion al usuario del viaje aceptado
        Notificacion notificacionPasajero = Notificacion.paraPasajero(
                boletoInicial.getPasajero().getId(),
                "Tu viaje de " + origen.getProvincia() + " a " + destino.getProvincia() + " ha sido aceptado por el conductor " + conductor.getNombre() + "."
        );
        // Enviar correo al pasajero
        String tripUrl = "https://interride.netlify.app";
        Map<String, Object> model = new HashMap<>();
        model.put("user", usuarioPasajero.getCorreo());
        model.put("conductor", conductor.getNombre() + " " + conductor.getApellidos());
        model.put("origen", origen.getProvincia());
        model.put("destino", destino.getProvincia());
        model.put("tripUrl", tripUrl);




        Mail mail = emailService.createEmail(
                usuarioPasajero.getCorreo(),
                "Viaje Aceptado",
                model,
                mailFrom
        );

        emailService.sendEmail(mail, "email/accepted-trip-template.html");

        notificacionRepository.save(notificacionPasajero);

        Viaje viajeAceptado = viajeRepository.save(viaje);

        return viajeMapper.toViajeAceptadoResponse(viajeAceptado, conductor, origen, destino);
    }

    @Override
    @Transactional
    public boolean empezarViaje(Integer idViaje, Integer idConductor) {

        // Verificar si el viaje existe
        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new ResourceNotFoundException("El viaje con ID " + idViaje + " no existe."));

        // Verificar si el conductor existe
        Conductor conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new ResourceNotFoundException("El conductor con ID " + idConductor + " no existe."));

        // Verificar que el conductor no tiene otro viaje en curso
        List<Viaje> viajesEnCurso = viajeRepository.findByConductorIdAndState(conductor.getId(), EstadoViaje.EN_CURSO);

        if (!viajesEnCurso.isEmpty()) {
            throw new BusinessRuleException("Ya tiene un viaje en curso.");
        }


        // Verificar si es la hora de inicio del viaje con margen de 30 minutos
        LocalDateTime horaInicioViaje = viaje.getFechaHoraPartida();
        LocalDateTime horaActual = LocalDateTime.now();
        /*if (horaActual.isBefore(horaInicioViaje.minusMinutes(30)) || horaActual.isAfter(horaInicioViaje.plusMinutes(30))) {
            throw new BusinessRuleException("El viaje con ID " + idViaje + " no puede comenzar ahora. La hora de inicio es: " + horaInicioViaje);
        }*/



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

        // Enviar notificación al conductor
        notificacionRepository.enviarNotificacionConductor(
                "El viaje con ID " + idViaje + " ha comenzado.",
                conductor.getId()
        );
        // Enviar notificación a los pasajeros y generar pagos pandientes
        for (PasajeroViaje boleto : boletos) {
            notificacionRepository.enviarNotificacionPasajero(
                    "El viaje con ID " + idViaje + " ha comenzado.",
                    boleto.getPasajero().getId()
            );

            /* Ahora el pago se crea cuando el pasajero aborda el viaje
            Pago pago = Pago.builder()
                    .estado(EstadoPago.PENDIENTE)
                    .monto(25.0)
                    .pasajero(boleto.getPasajero())
                    .conductor(conductor)
                    .viaje(viaje)
                    .fechaHoraPago(LocalDateTime.now())
                    .build();

            pagoRepository.save(pago);

             */
        }

        // Crear la respuesta
        return true;
    }

    @Override
    @Transactional
    public ViajeSolicitadoResponse crearViajeSolicitado(Integer pasajeroId, ViajeSolicitadoRequest request) {
        Pasajero pasajero = pasajeroRepository.findById(pasajeroId)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con id: " + pasajeroId));

        Viaje viaje = viajeMapper.toEntity(request);
        Pair<Ubicacion, Ubicacion> origenANDdestino = ubicacionMapper.OrigenDestinotoEntity(request);
        Ubicacion origen = origenANDdestino.getFirst();
        Ubicacion destino = origenANDdestino.getSecond();
        // Eliminar los valores numericos al final de las provincias
        origen.setProvincia(origen.getProvincia().replaceAll("\\s*\\d+$", ""));
        destino.setProvincia(destino.getProvincia().replaceAll("\\s*\\d+$", ""));

        PasajeroViaje boleto = pasajeroViajeMapper.toEntity(request);

        if(boleto.getAsientosOcupados() <= 0){
            throw new BusinessRuleException("El número de asientos ocupados debe ser mayor a 0.");
        }

        //Creacion del viaje
        Viaje viajeSolicitado = viajeRepository.save(viaje);

        //Ajustes del boleto
        boleto.setPasajero(pasajero);
        boleto.setViaje(viajeSolicitado);


        //boleto.setCosto(25.0); //Falta implementar logica para el costo real

        BigDecimal distancia = new BigDecimal(Math.sqrt(origen.getLatitud().pow(2).add(origen.getLongitud().pow(2)).doubleValue()))
                .setScale(2, RoundingMode.HALF_UP);
        boleto.setCosto(distancia.multiply(new BigDecimal("0.5")).doubleValue());

        boleto.setFechaHoraLLegada(viaje.getFechaHoraPartida().plusHours(4));//Falta implementar logica para la fecha de llegada real

        //Guardar el boleto
        PasajeroViaje boletoCreado = pasajeroViajeRepository.save(boleto);

        //Ajustes en las ubicaciones
        //El origen solo pertence a un viaje
        //El destino pertence a un boleto
        origen.setViaje(viajeSolicitado);
        destino.setPasajeroViaje(boletoCreado);

        //Guardar en ubicaciones

        Ubicacion origenCreado = ubicacionRepository.save(origen);
        Ubicacion destinoCreado = ubicacionRepository.save(destino);


        return viajeMapper.toViajeSolicitadoResponse(viajeSolicitado, boletoCreado, origenCreado, destinoCreado);
    }


    @Override
    @Transactional(readOnly = true)
    public ViajeCompletadoConductorResponse verDetalleViajeCompletadoPorConductor(Integer viajeId) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con ID: " + viajeId));

        List<PasajeroViaje> pasajerosViaje = pasajeroViajeRepository.findPasajerosCompletadosByViajeId(viajeId);
        if (pasajerosViaje.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron pasajeros para este viaje.");
        }

        Ubicacion ubicacion = ubicacionRepository.findByViajeId(viajeId);
        List<Calificacion> calificaciones = calificacionRepository.findByViajeId(viajeId);

        return ViajeMapper.toDetalleViajeConductorResponse(viaje, ubicacion, pasajerosViaje, calificaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViajeSolicitadoResponse> obtenerViajesSolicitados(){
        List<Viaje> viajesSolicitados = viajeRepository.findByEstado(EstadoViaje.SOLICITADO);
        if (viajesSolicitados.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron viajes solicitados.");
        }

        return viajesSolicitados.stream()
                .map(viaje -> {
                    PasajeroViaje boleto = pasajeroViajeRepository.findBoletoInicialIdByViajeId(viaje.getId());
                    Ubicacion origen = ubicacionRepository.findByViajeId(viaje.getId());
                    Ubicacion destino = ubicacionRepository.findByPasajeroViajeId(boleto.getId());
                    return viajeMapper.toViajeSolicitadoResponse(viaje, boleto, origen, destino);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ViajeSolicitadoResponse obtenerDetalleViajeSolicitado(Integer idViaje) {
        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new ResourceNotFoundException("El viaje solicitado con ID " + idViaje + " no existe."));

        if (viaje.getEstado() != EstadoViaje.SOLICITADO) {
            throw new BusinessRuleException("El viaje con ID " + idViaje + " no está en estado SOLICITADO.");
        }

        PasajeroViaje boleto = pasajeroViajeRepository.findBoletoInicialIdByViajeId(idViaje);
        if (boleto == null) {
            throw new ResourceNotFoundException("No se encontró un boleto inicial para el viaje con ID " + idViaje + ".");
        }

        Ubicacion origen = ubicacionRepository.findByViajeId(idViaje);
        Ubicacion destino = ubicacionRepository.findByPasajeroViajeId(boleto.getId());

        return viajeMapper.toViajeSolicitadoResponse(viaje, boleto, origen, destino);
    }

    @Override
    @Transactional(readOnly = true)
    public ViajeEnCursoResponse getViajeAceptadoByPasajeroId(Integer pasajeroId) {
        List<Object[]> obj = viajeRepository.getViajeAceptadoByPasajeroId(pasajeroId);

        if (obj.isEmpty()) {
            throw new ResourceNotFoundException("No hay viajes aceptados para el pasajero con id: " + pasajeroId);
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
    @Transactional(readOnly = true)
    public List<ViajeAceptadoResponse> obtenerViajesAceptadosPorConductor(Integer idConductor){
        Conductor conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new ResourceNotFoundException("El conductor con ID " + idConductor + " no existe."));

        List<Viaje> viajesAceptados = viajeRepository.findByConductorIdAndEstado(conductor.getId(), EstadoViaje.ACEPTADO);
        List<Ubicacion> origenes = new ArrayList<>();
        List<Ubicacion> destinos = new ArrayList<>();

        List<ViajeAceptadoResponse> viajeAceptadoResponses = new ArrayList<>();

        if (viajesAceptados.isEmpty()) {
            return viajeAceptadoResponses; // Retorna una lista vacía si no hay viajes aceptados
        }

        for (Viaje viaje : viajesAceptados) {
            Ubicacion origen = ubicacionRepository.findByViajeId(viaje.getId());
            Ubicacion destino = ubicacionRepository.findByPasajeroViajeId(pasajeroViajeRepository.findBoletoInicialIdByViajeId(viaje.getId()).getId());
            origenes.add(origen);
            destinos.add(destino);
        }



        for (int i = 0; i < viajesAceptados.size(); i++) {
            Viaje viaje = viajesAceptados.get(i);
            Ubicacion origen = origenes.get(i);
            Ubicacion destino = destinos.get(i);
            viajeAceptadoResponses.add(viajeMapper.toViajeAceptadoResponse(viaje, conductor, origen, destino));
        }



        return viajeAceptadoResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public ViajeAceptadoResponse getViajeAceptadoById(Integer viajeId){
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new ResourceNotFoundException("El viaje con ID " + viajeId + " no existe."));

        Conductor conductor = conductorRepository.findById(viaje.getConductor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("El conductor con ID " + viaje.getConductor().getId() + " no existe."));

        if(viaje.getEstado() != EstadoViaje.ACEPTADO) {
            throw new BusinessRuleException("El viaje con ID " + viajeId + " no está en estado ACEPTADO.");
        }

        Ubicacion origen= ubicacionRepository.findByViajeId(viaje.getId());
        Ubicacion destino = ubicacionRepository.findByPasajeroViajeId(pasajeroViajeRepository.findBoletoInicialIdByViajeId(viaje.getId()).getId());



        return viajeMapper.toViajeAceptadoResponse(viaje, conductor, origen, destino);
    }

    @Override
    @Transactional(readOnly = true)
    public ViajeEnCursoResponse obtenerDetalleViajeEnCursoByConductorId(Integer idConductor) {
        List<Object[]> obj = viajeRepository.getViajeEnCursoByConductorId(idConductor);

        if (obj.isEmpty()) {
            throw new ResourceNotFoundException("No hay viajes en curso para el conductor con id: " + idConductor);
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
}

