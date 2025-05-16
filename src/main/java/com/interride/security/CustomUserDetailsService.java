package com.interride.security;

import com.interride.model.entity.Pasajero;
import com.interride.repository.PasajeroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PasajeroRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Pasajero p = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return UserPrincipal.create(p);
    }

    public UserDetails loadUserById(Integer id) {
        Pasajero p = repo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return UserPrincipal.create(p);
    }
}
