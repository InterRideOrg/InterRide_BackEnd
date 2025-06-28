package com.interride.controller;

import com.interride.dto.request.ActualizarUsuarioPerfilRequest;
import com.interride.dto.response.ConductorPerfilActualizadoResponse;
import com.interride.service.ConductorService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.interride.dto.response.ConductorPerfilPublicoResponse;
import com.interride.dto.response.NotificacionConductorResponse;
import com.interride.service.NotificacionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/conductor")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR')")
public class ConductorController {
    private final ConductorService conductorService;
    private final NotificacionService notificacionService;

    //Se utiliza para eliminar las notificaciones antiguas del pasajero al iniciar la aplicación
    @GetMapping("{id}/inicio")
    public ResponseEntity<String> inicioApp(@PathVariable int id) {
        notificacionService.eliminarNotificacionesAntiguasConductor(id);
        return ResponseEntity.ok("Inicio exitoso de la aplicación para el conductor con ID: " + id);
    }

    @GetMapping("{id}/notificaciones")
    public ResponseEntity<List<NotificacionConductorResponse>> obtenerNotificacionesConductor(@PathVariable Integer id) {
        List<NotificacionConductorResponse> notificaciones = notificacionService.obtenerNotificacionesConductor(id);
        return ResponseEntity.ok(notificaciones);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR','PASAJERO')")
    @GetMapping("/perfil-publico/{idViaje}")
    public ResponseEntity<ConductorPerfilPublicoResponse> obtenerPerfilConductorAsignado(@PathVariable Integer idViaje) {
        ConductorPerfilPublicoResponse response = conductorService.obtenerPerfilConductorAsignado(idViaje);
        return ResponseEntity.ok(response);
    }



}