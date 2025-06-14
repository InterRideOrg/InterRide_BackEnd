package com.interride.service.Impl;

import com.interride.dto.request.UbicacionRequest;
import com.interride.dto.response.BoletoCanceladoResponse;
import com.interride.dto.response.BoletoCompletadoResponse;
import com.interride.dto.response.BoletoUnionResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.PasajeroViajeMapper;
import com.interride.mapper.UbicacionMapper;
import com.interride.model.entity.*;
import com.interride.model.enums.EstadoViaje;
import com.interride.repository.NotificacionRepository;
import com.interride.repository.PasajeroViajeRepository;
import com.interride.repository.UbicacionRepository;
import com.interride.repository.ViajeRepository;
import com.interride.service.PasajeroViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PasajeroViajeServiceImpl implements PasajeroViajeService {
    private final PasajeroViajeRepository pasajeroViajeRepository;
    private final ViajeRepository viajeRepository;
    private final UbicacionRepository ubicacionRepository;
    private final NotificacionRepository notificacionRepository;

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
                        "El viaje con ID " + viaje.getId() + " ha sido cancelado debido a la falta de pasajeros.",
                        viaje.getConductor().getId()
                );
            }

            Integer asientosOcupadosAntiguos = boleto.getAsientosOcupados();
            viaje.setAsientosOcupados(viaje.getAsientosOcupados() - asientosOcupadosAntiguos);
            viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() + asientosOcupadosAntiguos);

            notificacionRepository.enviarNotificacionPasajero(
                    "Tu boleto con ID " + boleto.getId() + " ha sido cancelado.",
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
            throw new BusinessRuleException("El pasajero ya está unido a este viaje.");
        }

        if(!viaje.getEstado().equals(EstadoViaje.ACEPTADO)){
            throw new BusinessRuleException("El viaje no está en un estado válido para unirse.");
        }

        if(boleto.getAsientosOcupados() > viaje.getAsientosDisponibles()){
            throw  new BusinessRuleException("No hay suficientes asientos disponibles en el viaje.");
        }

        viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - boleto.getAsientosOcupados());
        viaje.setAsientosOcupados(viaje.getAsientosOcupados() + boleto.getAsientosOcupados());

        boleto.setFechaHoraUnion(LocalDateTime.now());
        boleto.setCosto(25.0); //Falta logica para calcular el costo real
        boleto.setFechaHoraLLegada(LocalDateTime.now().plusDays(2)); //Falta logica para calcular la fecha de llegada real
        boleto.setEstado(EstadoViaje.ACEPTADO);

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



        return pasajeroViajeMapper.toBoletoResponse(
                boleto,
                "Viaje de " + boleto.getPasajero().getUsername() + " finalizado. Monto recibido: S/" + formatoCosto
        );
    }


    //Funciones extra

    Integer cantidadBoletoEnCursoPorViaje(Integer viajeId) {
        return viajeRepository.cantidadBoletosEnCursoPorViaje(viajeId);
    }
}
