package com.interride.service;

import com.interride.dto.request.ActualizarUsuarioPerfilRequest;
import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.request.LoginRequest;
import com.interride.dto.request.PasajeroRegistrationRequest;

import com.interride.dto.response.AuthResponse;
import com.interride.dto.response.UsuarioResponse;
import com.interride.model.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

    UsuarioResponse registrarPasajero(PasajeroRegistrationRequest request);
    UsuarioResponse registrarConductor(ConductorRegistroRequest request);
    UsuarioResponse obtenerUsuarioPorId(Integer id);
    UsuarioResponse actualizarUsuario(Integer id, ActualizarUsuarioPerfilRequest request);
    UsuarioResponse obtenerPorConductorId(Integer conductorId);
    UsuarioResponse obtenerPorPasajeroId(Integer pasajeroId);
    Integer obtenerPasajeroIdPorUsuarioId(Integer userId);
    Integer obtenerConductorIdPorUsuarioId(Integer userId);
    Optional<Usuario> findByCorreo(String correo);
    AuthResponse login(LoginRequest request);


    //Authenticate user (login)
}
