package com.interride.controller;

import com.interride.service.PasajeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pasajero/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final PasajeroService pasajeroService;
/*    private final ConductorService conductorService;


    //Actualizar el perfil del pasajero
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> updatePasajeroProfile(@PathVariable Integer id, @RequestBody UserProfileDTO userProfileDTO) {
        UserProfileDTO updatedPasajeroProfile = pasajeroService.updatePasajeroProfile(id, userProfileDTO);
        return new ResponseEntity<>(updatedPasajeroProfile, HttpStatus.OK);
    }
    //Actualizar el perfil del conductor
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> updateConductorProfile(@PathVariable Integer id, @RequestBody UserProfileDTO userProfileDTO) {
        UserProfileDTO updatedConductorProfile = conductorService.updateConductorProfile(id, userProfileDTO);
        return new ResponseEntity<>(updatedConductorProfile, HttpStatus.OK);
    }

    //Obtener el perfil del pasajero por su ID
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getPasajeroProfile(@PathVariable Integer id) {
        UserProfileDTO pasajeroProfile = pasajeroService.getPasajeroProfile(id);
        return new ResponseEntity<>(pasajeroProfile, HttpStatus.OK);
    }


    //Obtener el perfil del conductor por su ID
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getConductorProfile(@PathVariable Integer id) {
        UserProfileDTO conductorProfile = conductorService.getConductorProfile(id);
        return new ResponseEntity<>(conductorProfile, HttpStatus.OK);
    }*/
}
