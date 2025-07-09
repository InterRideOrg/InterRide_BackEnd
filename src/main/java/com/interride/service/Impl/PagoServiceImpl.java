package com.interride.service.Impl;

import com.interride.dto.request.CreatePagoRequest;
import com.interride.dto.request.UpdatePagoRequest;
import com.interride.dto.response.AnnualProfitReport;
import com.interride.dto.response.MonthlyProfitReport;
import com.interride.dto.response.PagoResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.PagoMapper;
import com.interride.model.entity.*;
import com.interride.model.enums.EstadoPago;
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
    private final ConductorRepository conductorRepository;
    private final NotificacionRepository notificacionRepository;

    private void validatePagoEfectivo(Pago pago) {
        Pasajero pasajero = pasajeroRepository.findById(pago.getPasajero().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con id: " + pago.getPasajero().getId()));

        Conductor conductor = conductorRepository.findById(pago.getConductor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con id: " + pago.getConductor().getId()));
    }

    private void validatePagoTarjeta(Pago pago, Tarjeta tarjeta) {
        Pasajero pasajero = pasajeroRepository.findById(pago.getPasajero().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con id: " + pago.getPasajero().getId()));

        Conductor conductor = conductorRepository.findById(pago.getConductor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con id: " + pago.getConductor().getId()));

        if(!tarjeta.getPasajero().getId().equals(pasajero.getId())){
            throw new BusinessRuleException("Al pasajero no le pertenece la tarjeta con id: " + tarjeta.getId());
        }
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
                        .orElseThrow(()->new ResourceNotFoundException("Tarjeta con id " + tarjetaId + " no encontrada" ));
        validatePagoTarjeta(pagoActual, tarjeta);
        pagoActual.setFechaHoraPago(LocalDateTime.now());
        Pago nuevoPago = pagoRepository.save(pagoActual);
        return pagoMapper.toResponse(nuevoPago);
    }

    @Transactional
    @Override
    public PagoResponse completarPago(Integer id) {
        Pago pagoActual = pagoRepository.findById(id)
                        .orElseThrow(()->new RuntimeException("Pago con id " + id + " no encontrado" ));
        Pasajero pasajero = pasajeroRepository.findById(pagoActual.getPasajero().getId())
                        .orElseThrow(()->new RuntimeException("Pasajero con id " + pagoActual.getPasajero().getId() + " no encontrado" ));

        pagoActual.setEstado(EstadoPago.COMPLETADO);
        pagoActual.setFechaHoraPago(LocalDateTime.now());

        String formatoPago = String.format("%.2f", pagoActual.getMonto());

        //Enviar notificacion al conductor y al pasajero
        Notificacion notificacionConductor = Notificacion.builder()
                .mensaje("Pago completado por el pasajero " + pasajero.getUsername() + " por un monto de S/" + formatoPago)
                .fechaHoraEnvio(LocalDateTime.now())
                .leido(false)
                .conductor(pagoActual.getConductor())
                .build();

        //El pago debe tener dos decimales

        Notificacion notificacionPasajero = Notificacion.builder()
                .mensaje("Tu pago por S/" + formatoPago + " ha sido completado exitosamente.")
                .fechaHoraEnvio(LocalDateTime.now())
                .leido(false)
                .pasajero(pasajero)
                .build();

        notificacionRepository.save(notificacionConductor);
        notificacionRepository.save(notificacionPasajero);

        Pago pagoCompletado = pagoRepository.save(pagoActual);
        return pagoMapper.toResponse(pagoCompletado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnualProfitReport> getAnnualProfitReportByConductor(Integer year, Integer conductorId){
        List<Object[]> results = pagoRepository.findPagosByYearGroupedByMonth(year, conductorId);
        return results.stream()
                .map(result -> new AnnualProfitReport(
                        ((Number) result[0]).intValue(), // mes
                        ((Number) result[1]).doubleValue() // total
                )).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyProfitReport> getMonthlyProfitReportByConductor(Integer year, Integer month, Integer conductorId){
        List<Object[]> results = pagoRepository.findPagosByMonthGroupedByDay(year, month, conductorId);
        return results.stream()
                .map(result -> new MonthlyProfitReport(
                        ((Number) result[0]).intValue(), // dia
                        ((Number) result[1]).doubleValue() // total
                )).toList();
    }
}
