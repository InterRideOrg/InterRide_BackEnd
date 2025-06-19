package com.interride.service.Impl;

import com.interride.dto.request.ActualizarUsuarioPerfilRequest;
import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.request.LoginRequest;
import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.dto.response.AuthResponse;
import com.interride.dto.response.UsuarioResponse;
import com.interride.exception.DuplicateResourceException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.UsuarioMapper;
import com.interride.model.entity.Conductor;
import com.interride.model.entity.Pasajero;
import com.interride.model.entity.Role;
import com.interride.model.entity.Usuario;
import com.interride.model.enums.ERole;
import com.interride.repository.ConductorRepository;
import com.interride.repository.PasajeroRepository;
import com.interride.repository.RoleRepository;
import com.interride.repository.UsuarioRepository;
import com.interride.security.TokenProvider;
import com.interride.security.UserPrincipal;
import com.interride.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasajeroRepository pasajeroRepository;
    private final ConductorRepository conductorRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UsuarioMapper usuarioMapper;


    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Transactional
    @Override
    public UsuarioResponse registrarPasajero(PasajeroRegistrationRequest request) {
        boolean existsByCorreo = usuarioRepository.existsByCorreo(request.getCorreo());
        boolean existsByUsername = pasajeroRepository.existsByUsername(request.getUsername());
        boolean existsByTelefono = pasajeroRepository.existsByTelefono(request.getTelefono());

        if(existsByCorreo){
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el correo: " + request.getCorreo()
            );
        }

        if(existsByUsername){
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el nombre de usuario: " + request.getUsername()
            );
        }

        if(existsByTelefono){
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el telefono: " + request.getTelefono()
            );
        }

        Role role = roleRepository.findByNombre(ERole.PASAJERO)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + ERole.PASAJERO));


        Usuario usuario = usuarioMapper.toEntity(request);
        String passwordEncoded = passwordEncoder.encode(request.getPassword());
        usuario.setPassword(passwordEncoded);
        usuario.setRole(role);

        Pasajero pasajero = Pasajero.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .telefono(request.getTelefono())
                .username(request.getUsername())
                .fechaHoraRegistro(LocalDateTime.now())
                .build();

        pasajero.setUsuario(usuario);
        usuario.setPasajero(pasajero);

        Usuario saved = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(saved, ERole.PASAJERO);
    }

    @Transactional
    @Override
    public UsuarioResponse registrarConductor(ConductorRegistroRequest request) {
        boolean existsByCorreo = usuarioRepository.existsByCorreo(request.correo());
        boolean existsByUsername = conductorRepository.existsByUsername(request.username());
        boolean existsByTelefono = conductorRepository.existsByTelefono(request.telefono());


        if(existsByCorreo){
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el correo: " + request.correo()
            );
        }

        if(existsByUsername){
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el nombre de usuario: " + request.username()
            );
        }

        if(existsByTelefono){
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el telefono: " + request.telefono()
            );
        }

        Role role = roleRepository.findByNombre(ERole.CONDUCTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + ERole.CONDUCTOR));

        Usuario usuario = usuarioMapper.toEntity(request);
        String passwordEncoded = passwordEncoder.encode(request.password());

        usuario.setPassword(passwordEncoded);
        usuario.setRole(role);

        Conductor conductor = Conductor.builder()
                .nombre(request.nombre())
                .apellidos(request.apellidos())
                .telefono(request.telefono())
                .username(request.username())
                .fechaHoraRegistro(LocalDateTime.now())
                .build();

        conductor.setUsuario(usuario);
        usuario.setConductor(conductor);

        Usuario saved = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(saved, ERole.CONDUCTOR);
    }

    @Transactional
    @Override
    public UsuarioResponse obtenerUsuarioPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        ERole role = usuario.getRole().getNombre();

        return usuarioMapper.toResponse(usuario, role);
    }

    @Transactional
    @Override
    public UsuarioResponse actualizarUsuario(Integer id, ActualizarUsuarioPerfilRequest request) {
        boolean existsByCorreo = usuarioRepository.existsByCorreo(request.correo());
        boolean existsByPasajeroUsername = pasajeroRepository.existsByUsername(request.username());
        boolean existsByPasajeroTelefono = pasajeroRepository.existsByTelefono(request.telefono());
        boolean existsByConductorUsername = conductorRepository.existsByUsername(request.username());
        boolean existsByConductorTelefono = conductorRepository.existsByTelefono(request.telefono());



        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        String username = (usuario.getConductor() != null) ? usuario.getConductor().getUsername() : usuario.getPasajero().getUsername();
        String telefono = (usuario.getConductor() != null) ? usuario.getConductor().getTelefono() : usuario.getPasajero().getTelefono();

        if(existsByCorreo && !usuario.getCorreo().equals(request.correo())) {
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el correo: " + request.correo()
            );
        }

        if(existsByPasajeroUsername && !username.equals(request.username())) {
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el nombre de usuario: " + request.username()
            );
        }

        if(existsByPasajeroTelefono && !telefono.equals(request.telefono())) {
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el telefono: " + request.telefono()
            );
        }

        if(existsByConductorUsername && !username.equals(request.username())) {
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el nombre de usuario: " + request.username()
            );
        }

        if(existsByConductorTelefono && !telefono.equals(request.telefono())) {
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el telefono: " + request.telefono()
            );
        }


        usuario.setCorreo(request.correo());

        if (usuario.getConductor() != null) {
            Conductor conductor = usuario.getConductor();
            conductor.setNombre(request.nombres());
            conductor.setApellidos(request.apellidos());
            conductor.setTelefono(request.telefono());
            conductor.setUsername(request.username());
            conductor.setFechaHoraActualizacion(LocalDateTime.now());
            usuario.setConductor(conductor);
        } else if (usuario.getPasajero() != null) {
            Pasajero pasajero = usuario.getPasajero();
            pasajero.setNombre(request.nombres());
            pasajero.setApellidos(request.apellidos());
            pasajero.setTelefono(request.telefono());
            pasajero.setUsername(request.username());
            pasajero.setFechaHoraActualizacion(LocalDateTime.now());
            usuario.setPasajero(pasajero);
        }

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        ERole role = updatedUsuario.getRole().getNombre();


        return usuarioMapper.toResponse(updatedUsuario, role);
    }

    @Transactional
    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
        );


        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Usuario usuario = userPrincipal.getUsuario();

        String token = tokenProvider.createAccessToken(authentication);

        return usuarioMapper.toAuthResponse(usuario, token);
    }
}
