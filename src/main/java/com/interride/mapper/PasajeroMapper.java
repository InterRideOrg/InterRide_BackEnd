package com.interride.mapper;


import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.dto.response.PasajeroRegistroResponse;
import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.model.entity.Pasajero;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasajeroMapper {

    private final ModelMapper mapper;

    public Pasajero toEntity(PasajeroRegistrationRequest dto) {
        return mapper.map(dto, Pasajero.class);
    }

    public PasajeroRegistroResponse toProfileDTO(Pasajero entity) {
        return mapper.map(entity, PasajeroRegistroResponse.class);
    }

    public PasajeroPerfilPublicoResponse toPublicProfileDTO(Pasajero entity) {
        return new PasajeroPerfilPublicoResponse(
                entity.getNombre(),
                entity.getApellidos(),
                entity.getTelefono(),
                entity.getUsername()
        );
    }
}