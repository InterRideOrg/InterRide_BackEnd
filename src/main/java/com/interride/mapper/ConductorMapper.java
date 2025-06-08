package com.interride.mapper;

import com.interride.dto.response.ConductorPerfilPublicoResponse;
import com.interride.model.entity.Conductor;

public class ConductorMapper {
    public static ConductorPerfilPublicoResponse toResponse(Conductor conductor) {
        return new ConductorPerfilPublicoResponse(
                conductor.getNombre(),
                conductor.getApellidos(),
                conductor.getCorreo(),
                conductor.getTelefono(),
                conductor.getVehiculo().getPlaca()
        );
    }
}
