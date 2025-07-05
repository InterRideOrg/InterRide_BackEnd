package com.interride.service.Impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.interride.dto.request.GoogleLoginRequest;
import com.interride.dto.response.AuthResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.model.entity.Usuario;
import com.interride.security.TokenProvider;
import com.interride.security.google.GoogleTokenVerifierUtil;
import com.interride.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GoogleLoginServiceImpl {

    private final UsuarioService usuarioService;
    private final TokenProvider tokenProvider;


    public AuthResponse loginWithGoogle(GoogleLoginRequest request) {
        // 1. Verifica token de Google
        GoogleIdToken.Payload payload = GoogleTokenVerifierUtil.verifyToken(request.getIdToken());
        if (payload == null || !Boolean.TRUE.equals(payload.getEmailVerified())) {
            throw new BusinessRuleException("Token inválido o email no verificado");
        }

        // 2. Busca usuario en tu base de datos
        String email = payload.getEmail();
        Usuario usuario = usuarioService.findByCorreo(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no registrado"));

        // 3. Crea Authentication manualmente usando rol
        String rol = usuario.getRole().getNombre().toString(); // Ej: "CONDUCTOR" o "PASAJERO"
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(usuario.getCorreo(), null, authorities);

        // 4. Genera el JWT personalizado
        String jwt = tokenProvider.createAccessToken(authentication);

        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setUsername(usuario.getId().toString());
        response.setRole(usuario.getRole().getNombre().toString());
        response.setUserId(usuario.getId());

        return response;
    }
}
