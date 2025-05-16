package com.interride.controller;

import com.interride.dto.request.ReclamoRequest;
import com.interride.dto.response.ReclamoResponse;
import com.interride.service.ReclamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/reclamo")
@RequiredArgsConstructor
public class ReclamoController {

    private final ReclamoService reclamoService;

    @PostMapping("/enviar")
    public ResponseEntity<ReclamoResponse> enviarReclamo(@RequestBody @Valid ReclamoRequest request) {
        ReclamoResponse response = reclamoService.crearReclamo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
