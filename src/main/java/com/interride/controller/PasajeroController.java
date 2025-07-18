package com.interride.controller;

import com.interride.dto.request.ActualizarPasajeroPerfilRequest;
import com.interride.dto.response.NotificacionPasajeroResponse;
import com.interride.dto.response.PasajeroPerfilPrivadoResponse;
import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.service.NotificacionService;
import com.interride.service.PasajeroService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pasajero")
@PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
public class PasajeroController {

    private final PasajeroService pasajeroService;
    private final NotificacionService notificacionService;

    //Se utiliza para eliminar las notificaciones antiguas del pasajero al iniciar la aplicación
    @GetMapping("/{id}/inicio")
    public ResponseEntity<String> inicioApp(@PathVariable Integer id) {
        notificacionService.eliminarNotificacionesAntiguasPasajero(id);
        return ResponseEntity.ok("Inicio exitoso de la aplicación para el pasajero con ID: " + id);
    }


    @GetMapping("/{id}/notificaciones")
    public ResponseEntity<List<NotificacionPasajeroResponse>> obtenerNotificacionesPasajero(@PathVariable Integer id) {
        List<NotificacionPasajeroResponse> notificaciones = notificacionService.obtenerNotificacionesPasajero(id);
        return ResponseEntity.ok(notificaciones);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO','CONDUCTOR')")
    @GetMapping("/{id}")
    public PasajeroPerfilPublicoResponse obtenerPerfilPublicoPasajero(@PathVariable Integer id) {
        return pasajeroService.obtenerPerfilPasajero(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    @GetMapping("/priv/{id}")
    public PasajeroPerfilPrivadoResponse obtenerPerfilPrivadoPasajero(@PathVariable Integer id) {
        return pasajeroService.obtenerPerfilPrivadoPasajero(id);
    }

}