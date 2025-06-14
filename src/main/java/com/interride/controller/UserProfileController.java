package com.interride.controller;

import com.interride.dto.request.ActualizarUsuarioPerfilRequest;
import com.interride.dto.response.UsuarioResponse;
import com.interride.model.entity.Usuario;
import com.interride.service.PasajeroService;
import com.interride.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//Usuario => Pasajero o Conductor

@RestController
@RequestMapping("/usuario/profile")
@RequiredArgsConstructor
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

}
