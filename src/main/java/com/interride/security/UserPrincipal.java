package com.interride.security;

import com.interride.model.entity.Pasajero;   // ← ajusta tu paquete entity si cambia
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final Pasajero pasajero;

    /*----------------------------------------------------------*/
    /*  FACTORY METHOD ESPERADO POR CustomUserDetailsService    */
    /*----------------------------------------------------------*/
    public static UserPrincipal create(Pasajero pasajero) {
        return new UserPrincipal(pasajero);
    }

    private UserPrincipal(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    /*----------------------------------------------------------*/
    /*  Si el id es Integer en tu entidad, devuelve Integer     */
    /*----------------------------------------------------------*/
    public Integer getId() {
        return pasajero.getId();
    }

    @Override public String getUsername()               { return pasajero.getCorreo(); }
    @Override public String getPassword()               { return pasajero.getPassword(); }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /* Métodos de contrato UserDetails */
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
