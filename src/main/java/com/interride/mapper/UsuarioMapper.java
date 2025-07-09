package com.interride.mapper;

import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.request.LoginRequest;
import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.dto.response.AuthResponse;
import com.interride.dto.response.UsuarioResponse;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Role;
import com.interride.model.entity.Usuario;
import com.interride.model.enums.ERole;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public Usuario toEntity(PasajeroRegistrationRequest request){
        if(request == null) {
            return null;
        }

        return Usuario.builder()
                .correo(request.getCorreo())
                .password(request.getPassword())
                .build();
    }

    public Usuario toEntity(ConductorRegistroRequest request){
        if(request == null) {
            return null;
        }
        return Usuario.builder()
                .correo(request.correo())
                .password(request.password())
                .build();
    }

    public UsuarioResponse toResponse(Usuario usuario, ERole role) {
        if (usuario == null) {
            return null;
        }

        if (role == ERole.PASAJERO){
            return new UsuarioResponse(
                    usuario.getId(),
                    usuario.getPasajero().getNombre(),
                    usuario.getPasajero().getApellidos(),
                    usuario.getCorreo(),
                    usuario.getPasajero().getTelefono(),
                    usuario.getPasajero().getUsername(),
                    usuario.getPasajero().getFechaHoraRegistro().toString(),
                    (usuario.getPasajero().getFechaHoraActualizacion() == null) ? "No actualizado" :
                    usuario.getPasajero().getFechaHoraActualizacion().toString(),
                    role.toString()
            );
        }else {
            return new UsuarioResponse(
                    usuario.getId(),
                    usuario.getConductor().getNombre(),
                    usuario.getConductor().getApellidos(),
                    usuario.getCorreo(),
                    usuario.getConductor().getTelefono(),
                    usuario.getConductor().getUsername(),
                    usuario.getConductor().getFechaHoraRegistro().toString(),
                    (usuario.getConductor().getFechaHoraActualizacion() == null) ? "No actualizado" :
                    usuario.getConductor().getFechaHoraActualizacion().toString(),
                    role.toString()
            );
        }
    }

    public Usuario toEntity(LoginRequest request) {
        if (request == null) {
            return null;
        }

        return Usuario.builder()
                .correo(request.getCorreo())
                .password(request.getPassword())
                .build();
    }

    public AuthResponse toAuthResponse(Usuario usuario, String token) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);

        String username = (usuario.getConductor() != null) ? usuario.getConductor().getUsername()
                : (usuario.getPasajero() != null) ? usuario.getPasajero().getUsername()
                : "Admin";

        authResponse.setUsername(username);
        authResponse.setRole(usuario.getRole().getNombre().toString());
        authResponse.setUserId(usuario.getId());
        return authResponse;
    }
}
