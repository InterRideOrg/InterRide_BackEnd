package com.interride.controller;

import com.interride.dto.request.ActualizarUsuarioPerfilRequest;
import com.interride.dto.response.UsuarioResponse;
import com.interride.model.entity.Usuario;
import com.interride.service.PasajeroService;
import com.interride.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


//Usuario => Pasajero o Conductor

@RestController
@RequestMapping("/usuario/profile")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CONDUCTOR', 'PASAJERO')")
public class UserProfileController {

    private final UsuarioService usuarioService;

    //Actualizar el perfil ya sea de pasajero o conductor
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> updateProfile(@PathVariable Integer id, @RequestBody ActualizarUsuarioPerfilRequest request) {
        UsuarioResponse response = usuarioService.actualizarUsuario(id, request);
        return ResponseEntity.ok(response);
    }

    //Obtener el perfil del usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getProfile(@PathVariable Integer id) {
        UsuarioResponse response = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pasajero/{id_pasajero}")
    @PreAuthorize("hasAnyRole('PASAJERO', 'CONDUCTOR')")
    public ResponseEntity<UsuarioResponse> getPasajeroProfile(@PathVariable Integer id_pasajero) {
        UsuarioResponse response = usuarioService.obtenerPorPasajeroId(id_pasajero);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conductor/{id_conductor}")
    @PreAuthorize("hasAnyRole('CONDUCTOR', 'PASAJERO')")
    public ResponseEntity<UsuarioResponse> getConductorProfile(@PathVariable Integer id_conductor) {
        UsuarioResponse response = usuarioService.obtenerPorConductorId(id_conductor);
        return ResponseEntity.ok(response);
    }

}
