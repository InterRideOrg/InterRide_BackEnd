package com.interride.mapper;

import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.response.ConductorPerfilPublicoResponse;
import com.interride.model.entity.Conductor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ConductorMapper {
    private final PasswordEncoder passwordEncoder;

    public Conductor toEntity(ConductorRegistroRequest request) {
        return Conductor.builder()
                .nombre(request.nombre())
                .apellidos(request.apellidos())
                .correo(request.correo())
                .password(passwordEncoder.encode(request.password()))
                .telefono(request.telefono())
                .username(request.username())
                .fechaHoraRegistro(LocalDateTime.now())
                .build();

    }

    public static ConductorPerfilPublicoResponse toResponse(Conductor conductor) {
        return new ConductorPerfilPublicoResponse(
                conductor.getNombre(),
                conductor.getApellidos(),
                conductor.getTelefono(),
                conductor.getVehiculo().getPlaca()
        );
    }
}
