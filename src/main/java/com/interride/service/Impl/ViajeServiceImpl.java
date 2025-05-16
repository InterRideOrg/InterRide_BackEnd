package com.interride.service.Impl;

import com.interride.dto.response.PasajeroViajesResponse;
import com.interride.model.enums.EstadoViaje;
import com.interride.repository.ViajeRepository;
import com.interride.service.ViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ViajeServiceImpl implements ViajeService {
    private final ViajeRepository viajeRepository;

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


}
