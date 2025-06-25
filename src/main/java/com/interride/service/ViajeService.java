package com.interride.service;


import com.interride.dto.request.ViajeSolicitadoRequest;
import com.interride.dto.response.*;


import java.time.LocalDate;
import java.util.List;


public interface ViajeService {
    List<PasajeroViajesResponse> getViajesByPasajeroId(Integer pasajeroId);
    List<PasajeroViajesResponse> getViajesCompletadosByPasajeroId(Integer pasajeroId);

    ViajeSolicitadoResponse crearViajeSolicitado(Integer pasajeroId, ViajeSolicitadoRequest request);

    DetalleViajeResponse obtenerDetalleViajeNoCancelado(Integer id, Integer idPasajero);
    DetalleViajeResponse obtenerDetalleViajeCancelado(Integer id);

    DetalleViajeResponse obtenerDetalleViaje(Integer idViaje, Integer idPasajero);
    ViajeEnCursoResponse obtenerDetalleViajeEnCurso(Integer idPasajero);
    ViajeCanceladoResponse cancelarViaje(Integer idViaje);

    List<ViajeDisponibleResponse> obtenerViajesDisponibles(String provinciaOrigen, String provinciaDestino, LocalDate fechaViaje);

    List<ViajeCompletadoResponse> obtenerViajesCompletados(Integer idConductor);

    ViajeAceptadoResponse aceptarViaje(Integer idViaje, Integer idConductor);

    boolean empezarViaje(Integer idViaje, Integer idConductor);

    ViajeCompletadoConductorResponse verDetalleViajeCompletadoPorConductor(Integer idViaje);

}

