package com.interride.mapper;

import com.interride.dto.response.PasajeroProfileResponse;
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

    public PasajeroProfileResponse toProfileDTO(Pasajero entity) {
        return mapper.map(entity, PasajeroProfileResponse.class);
    }
}