package com.interride.service;
import com.interride.dto.response.PasajeroPerfilPublicoResponse;

public interface PasajeroService {
    PasajeroPerfilPublicoResponse obtenerPerfilPasajero(Integer idPasajero);
}
