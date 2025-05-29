package com.interride.service;


import com.interride.dto.response.*;


import java.time.LocalDate;
import java.util.List;


public interface ViajeService {
    List<PasajeroViajesResponse> getViajesByPasajeroId(Integer pasajeroId);


    DetalleViajeResponse obtenerDetalleViajeNoCancelado(Integer id, Integer idPasajero);
    DetalleViajeResponse obtenerDetalleViajeCancelado(Integer id);

    DetalleViajeResponse obtenerDetalleViaje(Integer idViaje, Integer idPasajero);
    ViajeEnCursoResponse obtenerDetalleViajeEnCurso(Integer idPasajero);
    boolean cancelarViaje(Integer idViaje);

    List<ViajeDisponibleResponse> obtenerViajesDisponibles(String provinciaOrigen, String provinciaDestino, LocalDate fechaViaje);

    List<ViajeCompletadoResponse> obtenerViajesCompletados(Integer idConductor);

    ViajeAceptadoResponse aceptarViaje(Integer idViaje, Integer idConductor);

}

