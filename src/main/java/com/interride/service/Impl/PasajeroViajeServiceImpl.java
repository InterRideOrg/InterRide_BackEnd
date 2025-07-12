package com.interride.service.Impl;

import com.interride.dto.request.UbicacionRequest;
import com.interride.dto.response.*;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.PasajeroViajeMapper;
import com.interride.mapper.UbicacionMapper;
import com.interride.model.entity.*;
import com.interride.model.enums.EstadoPago;
import com.interride.model.enums.EstadoViaje;
import com.interride.repository.*;
import com.interride.service.PasajeroViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PasajeroViajeServiceImpl implements PasajeroViajeService {
    private final PasajeroViajeRepository pasajeroViajeRepository;
    private final ViajeRepository viajeRepository;
    private final UbicacionRepository ubicacionRepository;
    private final NotificacionRepository notificacionRepository;
    private final PasajeroRepository pasajeroRepository;
    private final PagoRepository pagoRepository;

    private final UbicacionMapper ubicacionMapper;
    private final PasajeroViajeMapper pasajeroViajeMapper;

    @Transactional
    @Override
    public BoletoCanceladoResponse cancelarBoleto(Integer id) {
        PasajeroViaje boleto = pasajeroViajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boleto no encontrado con id: " + id));
        Viaje viaje = viajeRepository.findById(boleto.getViaje().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con id: " + boleto.getViaje().getId()));

        Ubicacion origen = ubicacionRepository.findByViajeId(viaje.getId());
        Ubicacion destino = ubicacionRepository.findByPasajeroViajeId(boleto.getId());


        List<PasajeroViaje> pasajeros = pasajeroViajeRepository.findPasajerosAceptadosByViajeId(viaje.getId());
        //Validaciones

        if(boleto.getEstado().equals(EstadoViaje.ACEPTADO)){
            if(pasajeros.size() == 1){
                viaje.setEstado(EstadoViaje.CANCELADO);
                notificacionRepository.enviarNotificacionConductor(
                        "Tu viaje hacia " + destino.getProvincia() + " ha sido cancelado debido a la falta de pasajeros.",
                        viaje.getConductor().getId()
                );
            }

            Integer asientosOcupadosAntiguos = boleto.getAsientosOcupados();
            viaje.setAsientosOcupados(viaje.getAsientosOcupados() - asientosOcupadosAntiguos);
            viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() + asientosOcupadosAntiguos);

            notificacionRepository.enviarNotificacionPasajero(
                    "Haz cancelado tu boleto de viaje hacia " + destino.getProvincia() + ".",
                    boleto.getPasajero().getId()
            );
            boleto.setEstado(EstadoViaje.CANCELADO);

        }else if(boleto.getEstado().equals(EstadoViaje.SOLICITADO)){
            viaje.setEstado(EstadoViaje.CANCELADO);
            boleto.setEstado(EstadoViaje.CANCELADO);
            notificacionRepository.enviarNotificacionPasajero(
                    "Tu solicitud de viaje hacia " + destino.getProvincia() + " ha sido cancelada.",
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

    @Transactional
    @Override
    public BoletoUnionResponse createBoletoUnion(Integer pasajeroId, Integer viajeId, Integer asientosOcupados, UbicacionRequest ubicacionRequest) {
        PasajeroViaje boleto = new PasajeroViaje();
        boleto.setPasajero(Pasajero.builder().id(pasajeroId).build());
        boleto.setViaje(Viaje.builder().id(viajeId).build());
        boleto.setAsientosOcupados(asientosOcupados);
        boleto.setAbordo(false);

        Ubicacion ubicacionDestino = ubicacionMapper.toEntity(ubicacionRequest);
        Viaje viaje = viajeRepository.findById(boleto.getViaje().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con id: " + boleto.getViaje().getId()));

        Pasajero pasajeroActual = boleto.getPasajero();
        List<PasajeroViaje> boletosExistentes = pasajeroViajeRepository.findPasajerosAceptadosByViajeId(viaje.getId());

        //Validaciones
        if(boletosExistentes.stream().anyMatch(b -> b.getPasajero().getId().equals(pasajeroActual.getId()))){
            throw new BusinessRuleException("Ya estas unido a este viaje!");
        }

        if(!viaje.getEstado().equals(EstadoViaje.ACEPTADO)){
            throw new BusinessRuleException("El viaje no está en un estado válido para unirse.");
        }

        if(boleto.getAsientosOcupados() > viaje.getAsientosDisponibles()){
            throw  new BusinessRuleException("Los asientos que requieres son mayores a los disponibles.");
        }

        if(asientosOcupados <= 0){
            throw new BusinessRuleException("Cantidad de asientos ocupado inválida.");
        }

        viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - boleto.getAsientosOcupados());
        viaje.setAsientosOcupados(viaje.getAsientosOcupados() + boleto.getAsientosOcupados());

        boleto.setFechaHoraUnion(LocalDateTime.now());

        boleto.setFechaHoraLLegada(viaje.getFechaHoraPartida().plusHours(4)); //Falta logica para calcular la fecha de llegada real
        boleto.setEstado(EstadoViaje.ACEPTADO);

        BigDecimal distancia = new BigDecimal(Math.sqrt(ubicacionRequest.latitud().pow(2).add(ubicacionRequest.longitud().pow(2)).doubleValue()))
                .setScale(2, RoundingMode.HALF_UP);
        boleto.setCosto(distancia.multiply(new BigDecimal("0.5")).doubleValue());

        PasajeroViaje boletoUnionCreado = pasajeroViajeRepository.save(boleto);

        ubicacionDestino.setPasajeroViaje(boletoUnionCreado);

        Ubicacion ubicacionDestinoGuardada = ubicacionRepository.save(ubicacionDestino);
        Ubicacion ubicacionOrigen = ubicacionRepository.findByViajeId(viaje.getId());



        return  new BoletoUnionResponse(
                boletoUnionCreado.getId(),
                boletoUnionCreado.getFechaHoraUnion().toString(),
                boletoUnionCreado.getCosto(),
                boletoUnionCreado.getFechaHoraLLegada().toString(),
                boletoUnionCreado.getPasajero().getId(),
                boletoUnionCreado.getViaje().getId(),
                boletoUnionCreado.getEstado().toString(),
                ubicacionOrigen.getDireccion(),
                ubicacionDestinoGuardada.getDireccion(),
                ubicacionOrigen.getProvincia(),
                ubicacionDestinoGuardada.getProvincia()
        );
    }

    @Transactional
    @Override
    public BoletoCompletadoResponse finalizarBoleto(Integer id){
        PasajeroViaje boleto = pasajeroViajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boleto no encontrado con id: " + id));

        if(!boleto.getEstado().equals(EstadoViaje.EN_CURSO)){
            throw new BusinessRuleException("Solo se puede finalizar un viaje en curso.");
        }

        Viaje viaje = boleto.getViaje();

        boleto.setEstado(EstadoViaje.COMPLETADO);

        String formatoCosto = String.format("%.2f", boleto.getCosto());

        //Notificacion al conductor del boleto finalizado
        Notificacion notificacionBoletoFinalizado = Notificacion.paraConductor(
                viaje.getConductor().getId(),
                "Viaje de " + boleto.getPasajero().getUsername() + " finalizado. Monto recibido: S/" + formatoCosto
                );

        notificacionRepository.save(notificacionBoletoFinalizado);


        //Si el boleto es el último en curso del viaje, se marca el viaje como completado
        Integer viajesRestantes = cantidadBoletoEnCursoPorViaje(boleto.getViaje().getId());
        if(viajesRestantes == 0){
            viaje.setEstado(EstadoViaje.COMPLETADO);
            Notificacion notificacionViajeCompletado = Notificacion.paraConductor(
                    viaje.getConductor().getId(),
                    "Viaje completado. Resumen de ingresos disponible en tu Billetera."
            );
            notificacionRepository.save(notificacionViajeCompletado);
        }



        return pasajeroViajeMapper.toBoletoCompletadoResponse(
                boleto,
                "Viaje de " + boleto.getPasajero().getUsername() + " finalizado. Monto recibido: S/" + formatoCosto
        );
    }

    @Transactional
    @Override
    public BoletoAbordoResponse abordarViaje(Integer pasajeroId, Integer viajeId) {
        PasajeroViaje boleto = pasajeroViajeRepository.findByPasajeroIdAndViajeId(pasajeroId, viajeId);
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con id: " + viajeId));

        Ubicacion destino = ubicacionRepository.findByPasajeroViajeId(boleto.getId());

        if(boleto == null) {
            throw new ResourceNotFoundException("Boleto no encontrado para el pasajero con id: " + pasajeroId + " y viaje con id: " + viajeId);
        }

        if(viaje.getConductor() == null) {
            throw new ResourceNotFoundException("El viaje no tiene un conductor asignado.");
        }

        if (!boleto.getEstado().equals(EstadoViaje.ACEPTADO)) {
            throw new BusinessRuleException("El boleto no está en un estado válido para abordar.");
        }

        if (Boolean.TRUE.equals(boleto.getAbordo()) ) {
            throw new BusinessRuleException("El pasajero ya está a bordo del viaje.");
        }

        Conductor conductor = viaje.getConductor();

        //Crear el pago
        Pago pago = Pago.builder()
                .estado(EstadoPago.PENDIENTE)
                .monto(boleto.getCosto())
                .pasajero(boleto.getPasajero())
                .conductor(conductor)
                .viaje(viaje)
                .fechaHoraPago(LocalDateTime.now())
                .build();

        //Notificacion al pasajero de que debe pagar
        Notificacion notificacionPago = Notificacion.paraPasajero(
                boleto.getPasajero().getId(),
                "Se ha generado un pago pendiente de S/25.00 por el viaje hacia " + destino.getProvincia() + "."
        );

        pagoRepository.save(pago);
        notificacionRepository.save(notificacionPago);


        boleto.setEstado(EstadoViaje.EN_CURSO);
        boleto.setAbordo(true);

        PasajeroViaje boletoActualizado = pasajeroViajeRepository.save(boleto);

        return new BoletoAbordoResponse(
                boletoActualizado.getId(),
                boletoActualizado.getAsientosOcupados(),
                boletoActualizado.getAbordo()
        );

    }

    @Override
    @Transactional(readOnly = true)
    public BoletoResponse getBoletoByPasajeroIdAndViajeId(Integer pasajeroId, Integer viajeId) {
        Pasajero pasajero = pasajeroRepository.findById(pasajeroId).
                orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con id: " + pasajeroId));

        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con id: " + viajeId));

        PasajeroViaje boleto = pasajeroViajeRepository.findByPasajeroIdAndViajeId(pasajero.getId(), viaje.getId());

        Ubicacion origen = ubicacionRepository.findByViajeId(viaje.getId());
        Ubicacion destino = ubicacionRepository.findByPasajeroViajeId(boleto.getId());

        return pasajeroViajeMapper.toBoletoResponse(boleto, origen, destino, viaje);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoletoResponse> getBoletosByPasajeroIdAndState(Integer pasajeroId, EstadoViaje state){
        Pasajero pasajero = pasajeroRepository.findById(pasajeroId)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con id: " + pasajeroId));

        List<BoletoResponse> boletosResponse = new ArrayList<>();

        List<PasajeroViaje> boletos = pasajeroViajeRepository.findByPasajeroIdAndEstado(pasajero.getId(), state);

        if (boletos.isEmpty()) {
           return boletosResponse; // Retorna una lista vacía si no hay boletos
        }

        Viaje viaje = viajeRepository.findById(boletos.getFirst().getViaje().getId()).
                orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con id: " + boletos.getFirst().getViaje().getId()));

        List<Ubicacion> origenes = new ArrayList<>();
        List<Ubicacion> destinos = new ArrayList<>();

        for (PasajeroViaje boleto : boletos) {
            origenes.add(ubicacionRepository.findByViajeId(viaje.getId()));
            destinos.add(ubicacionRepository.findByPasajeroViajeId(boleto.getId()));
        }


        for (int i = 0; i < boletos.size(); i++) {
            boletosResponse.add(pasajeroViajeMapper.toBoletoResponse(boletos.get(i), origenes.get(i), destinos.get(i), viaje));
        }

        return boletosResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoletoResponse> getBoletosByViajeId(Integer viajeId){
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con id: " + viajeId));

        List<PasajeroViaje> boletos = pasajeroViajeRepository.findByViajeId(viaje.getId());

        List<Ubicacion> origenes = new ArrayList<>();
        List<Ubicacion> destinos = new ArrayList<>();

        for (PasajeroViaje boleto : boletos) {
            origenes.add(ubicacionRepository.findByViajeId(viaje.getId()));
            destinos.add(ubicacionRepository.findByPasajeroViajeId(boleto.getId()));
        }

        List<BoletoResponse> boletosResponse = new ArrayList<>();

        for (int i = 0; i < boletos.size(); i++) {
            boletosResponse.add(pasajeroViajeMapper.toBoletoResponse(boletos.get(i), origenes.get(i), destinos.get(i), viaje));
        }

        return boletosResponse;
    }


    //Funciones extra

    Integer cantidadBoletoEnCursoPorViaje(Integer viajeId) {
        return viajeRepository.cantidadBoletosEnCursoPorViaje(viajeId);
    }



}
