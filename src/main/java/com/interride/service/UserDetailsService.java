package com.interride.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Enlaza tu dominio con Spring Security.
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

    /**
     * Permite buscar por correo o por username.
     */
    @Override
    UserDetails loadUserByUsername(String login) throws UsernameNotFoundException;
}
