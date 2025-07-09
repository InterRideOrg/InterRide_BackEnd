package com.interride.mapper;

import com.interride.dto.request.TarjetaRequest;
import com.interride.dto.response.TarjetaConductorResponse;
import com.interride.dto.response.TarjetaPasajeroResponse;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Tarjeta;
import org.springframework.stereotype.Component;

@Component
public class TarjetaMapper {
    public Tarjeta toEntityPasajero(TarjetaRequest request) {
        if (request == null) {
            return null;
        }

        return Tarjeta.builder()
                .numeroTarjeta(request.numeroTarjeta())
                .nombreTitular(request.nombreTitular())
                .fechaVencimiento(request.fechaExpiracion())
                .cvv(request.codigoSeguridad())
                .correo("encoded@enconded")
                .saldo(0.0)
                .build();
    }

    public Tarjeta toEntityConductor(TarjetaRequest request) {
        if (request == null) {
            return null;
        }

        return Tarjeta.builder()
                .numeroTarjeta(request.numeroTarjeta())
                .nombreTitular(request.nombreTitular())
                .fechaVencimiento(request.fechaExpiracion())
                .cvv(request.codigoSeguridad())
                .correo("encoded@enconded")
                .saldo(0.0)
                .build();
    }

    public TarjetaPasajeroResponse toPasajeroResponse(Tarjeta tarjeta) {
        if (tarjeta == null) {
            return null;
        }

        return new TarjetaPasajeroResponse(
                tarjeta.getId(),
                tarjeta.getNumeroTarjeta(),
                tarjeta.getNombreTitular(),
                tarjeta.getFechaVencimiento(),
                tarjeta.getCvv(),
                tarjeta.getPasajero().getId()
        );
    }

    public TarjetaConductorResponse toConductorResponse(Tarjeta tarjeta) {
        if (tarjeta == null) {
            return null;
        }

        return new TarjetaConductorResponse(
                tarjeta.getId(),
                tarjeta.getNumeroTarjeta(),
                tarjeta.getNombreTitular(),
                tarjeta.getFechaVencimiento(),
                tarjeta.getCvv(),
                tarjeta.getConductor().getId()
        );
    }

}
