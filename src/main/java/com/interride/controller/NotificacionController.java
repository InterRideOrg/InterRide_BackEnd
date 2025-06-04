package com.interride.controller;

import com.interride.dto.response.NotificacionResponse;
import com.interride.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @DeleteMapping("/{id}")
    public ResponseEntity<NotificacionResponse> deleteNotificacion(@PathVariable("id") Integer id){
        notificacionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
