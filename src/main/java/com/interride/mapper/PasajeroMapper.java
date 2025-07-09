package com.interride.mapper;


import com.interride.dto.response.PasajeroPerfilPrivadoResponse;
import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.dto.response.PasajeroRegistroResponse;
import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Usuario;
import com.interride.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasajeroMapper {

    private final ModelMapper mapper;
    private final UsuarioRepository usuarioRepository;

    public Pasajero toEntity(PasajeroRegistrationRequest dto) {
        return mapper.map(dto, Pasajero.class);
    }


    public PasajeroPerfilPublicoResponse toPublicProfileDTO(Pasajero entity) {
        return new PasajeroPerfilPublicoResponse(
                entity.getNombre(),
                entity.getApellidos(),
                entity.getTelefono(),
                entity.getUsername()
        );
    }


    public PasajeroPerfilPrivadoResponse toPrivateProfileDTO(Pasajero entity) {
        Usuario user = usuarioRepository.getUsuarioById(entity.getId());
        return new PasajeroPerfilPrivadoResponse(
                entity.getNombre(),
                entity.getApellidos(),
                entity.getTelefono(),
                user.getCorreo(),
                entity.getUsername()
        );
    }
}