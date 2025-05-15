package com.interride.mapper;

import com.interride.dto.PasajeroProfileDTO;
import com.interride.dto.PasajeroRegistrationDTO;
import com.interride.model.entity.Pasajero;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasajeroMapper {

    private final ModelMapper mapper;

    public Pasajero toEntity(PasajeroRegistrationDTO dto) {
        return mapper.map(dto, Pasajero.class);
    }

    public PasajeroProfileDTO toProfileDTO(Pasajero entity) {
        return mapper.map(entity, PasajeroProfileDTO.class);
    }
}