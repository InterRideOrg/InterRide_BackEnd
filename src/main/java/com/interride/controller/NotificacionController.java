package com.interride.controller;

import com.interride.dto.response.NotificacionConductorResponse;
import com.interride.dto.response.NotificacionPasajeroResponse;
import com.interride.dto.response.NotificacionResponse;
import com.interride.dto.response.NotificacionSimpleResponse;
import com.interride.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR', 'PASAJERO')")
    public ResponseEntity<NotificacionResponse> deleteNotificacion(@PathVariable Integer id){
        notificacionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/pasajero/{pasajeroId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASAJERO')")
    public ResponseEntity<List<NotificacionSimpleResponse>> listarPorPasajero(
            @PathVariable Integer pasajeroId,
            @RequestParam(defaultValue = "desc") String orden // "asc" o "desc"
    ) {
        return ResponseEntity.ok(notificacionService.listarPorPasajero(pasajeroId, orden));
    }

    @GetMapping("/conductor/{conductorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONDUCTOR')")
    public ResponseEntity<List<NotificacionSimpleResponse>> listarPorConductor(
            @PathVariable Integer conductorId,
            @RequestParam(defaultValue = "desc") String orden // "asc" o "desc"
    ) {
        return ResponseEntity.ok(notificacionService.listarPorConductor(conductorId, orden));
    }


}
