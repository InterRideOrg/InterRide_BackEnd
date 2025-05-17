package com.interride.service.Impl;

import com.interride.dto.request.CreatePagoRequest;
import com.interride.dto.request.UpdatePagoRequest;
import com.interride.dto.response.PagoResponse;
import com.interride.mapper.PagoMapper;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pago;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Tarjeta;
import com.interride.repository.*;
import com.interride.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PagoServiceImpl implements PagoService {
    private final PagoMapper pagoMapper;

    private final PagoRepository pagoRepository;
    private final TarjetaRepository tarjetaRepository;
    private final PasajeroRepository pasajeroRepository;
    private final ViajeRepository viajeRepository;
    private final ConductorRepository conductorRepository;

    private void validatePagoEfectivo(Pago pago) {
        Pasajero pasajero = pasajeroRepository.findById(pago.getPasajero().getId())
                .orElseThrow(() -> new RuntimeException("Pasajero no encontrado con id: " + pago.getPasajero().getId()));

        Conductor conductor = conductorRepository.findById(pago.getConductor().getId())
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado con id: " + pago.getConductor().getId()));
    }

    private void validatePagoTarjeta(Pago pago, Tarjeta tarjeta) {
        Pasajero pasajero = pasajeroRepository.findById(pago.getPasajero().getId())
                .orElseThrow(() -> new RuntimeException("Pasajero no encontrado con id: " + pago.getPasajero().getId()));

        Conductor conductor = conductorRepository.findById(pago.getConductor().getId())
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado con id: " + pago.getConductor().getId()));

        if(!tarjeta.getPasajero().getId().equals(pasajero.getId())){
            throw new RuntimeException("El pasajero no tiene la tarjeta con id: " + tarjeta.getId());
        }

        Tarjeta tarjetaEncontrada = tarjetaRepository.findById(tarjeta.getId())
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada con id: " + tarjeta.getId()));

    }

    @Transactional(readOnly = true)
    @Override
    public List<PagoResponse> getPagosByPasajeroId(Integer pasajeroId) {
        List<Pago> pagos = pagoRepository.findByPasajeroId(pasajeroId);
        return pagos.stream().map(pagoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PagoResponse> getPagosByConductorId(Integer conductorId){
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PagoResponse> getPagosByViajeId(Integer viajeId){
        return null;
    }

    @Transactional
    @Override
    public PagoResponse createPagoEfectivo(CreatePagoRequest pago) {
        Pago pagoActual = pagoMapper.toEntity(pago);
        validatePagoEfectivo(pagoActual);
        pagoActual.setFechaHoraPago(LocalDateTime.now());
        Pago nuevoPago = pagoRepository.save(pagoActual);
        return pagoMapper.toResponse(nuevoPago);
    }

    @Transactional
    @Override
    public PagoResponse createPagoTarjeta(CreatePagoRequest pago, Integer tarjetaId) {
        Pago pagoActual = pagoMapper.toEntity(pago);
        Tarjeta tarjeta = tarjetaRepository.findById(tarjetaId)
                        .orElseThrow(()->new RuntimeException("Tarjeta con id " + tarjetaId + " no encontrada" ));
        validatePagoTarjeta(pagoActual, tarjeta);
        pagoActual.setFechaHoraPago(LocalDateTime.now());
        Pago nuevoPago = pagoRepository.save(pagoActual);
        return pagoMapper.toResponse(nuevoPago);
    }

    @Transactional
    @Override
    public PagoResponse updatePago(Integer id, UpdatePagoRequest request) {
        Pago pagoActual = pagoRepository.findById(id)
                        .orElseThrow(()->new RuntimeException("Pago con id " + id + " no encontrado" ));

        pagoActual.setEstado(request.estadoPago());
        pagoActual.setFechaHoraPago(LocalDateTime.now());

        Pago pagoActualizado = pagoRepository.save(pagoActual);
        return pagoMapper.toResponse(pagoActualizado);
    }
}
