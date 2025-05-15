package com.interride.service.Impl;   // ← corrige si tu paquete tiene otra capitalización

import com.interride.model.entity.Pasajero;
import com.interride.repository.PasajeroRepository;
import com.interride.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasajeroRepository pasajeroRepository;

    public UserDetailsServiceImpl(PasajeroRepository pasajeroRepository) {
        this.pasajeroRepository = pasajeroRepository;
    }

    /*----------------------------------------------------------
     *  Carga por correo (username)  – usa UserPrincipal.create
     *----------------------------------------------------------*/
    @Override
    public UserDetails loadUserByUsername(String correo)
            throws UsernameNotFoundException {

        Pasajero pasajero = pasajeroRepository
                .findByCorreoIgnoreCase(correo)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No existe usuario con correo: " + correo));

        return UserPrincipal.create(pasajero);
    }

    /*----------------------------------------------------------
     *  Carga por id  – lo usa JWTFilter
     *----------------------------------------------------------*/
    public UserDetails loadUserById(Integer id) {
        Pasajero pasajero = pasajeroRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No existe usuario con id: " + id));

        return UserPrincipal.create(pasajero);
    }
}
