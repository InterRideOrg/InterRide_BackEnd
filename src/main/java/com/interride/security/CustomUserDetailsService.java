package com.interride.security;

import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Usuario;
import com.interride.repository.PasajeroRepository;
import com.interride.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRole().getNombre());

        return new UserPrincipal(
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getPassword(),
                Collections.singletonList(authority),
                usuario
        );
    }
}
