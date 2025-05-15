package com.interride.controller;


import com.interride.dto.response.PasajeroViajesResponse;
import com.interride.service.ViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class ViajeController {
    private final ViajeService viajeService;

    @GetMapping("/{id}/history")
    public ResponseEntity<List<PasajeroViajesResponse>> getViajesByPasajeroId(@PathVariable Integer id) {
        List<PasajeroViajesResponse> response = viajeService.getViajesByPasajeroId(id);
        return ResponseEntity.ok(response);
    }
}
