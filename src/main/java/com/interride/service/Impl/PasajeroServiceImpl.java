package com.interride.service.Impl;

import com.interride.model.entity.Pasajero;
import com.interride.repository.PasajeroRepository;
import com.interride.service.EmailService;
import com.interride.service.PasajeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PasajeroServiceImpl implements PasajeroService {
    private final PasajeroRepository pasajeroRepository;
    private final EmailService emailService;

    @Transactional
    @Override
    public Pasajero registerPasajero(Pasajero pasajero){
        if(pasajeroRepository.existsByCorreo(pasajero.getCorreo())){
            throw new RuntimeException("El email ya está registrado");
        }
        if(pasajeroRepository.existsByTelefono(pasajero.getTelefono())){
            throw new RuntimeException("El teléfono ya está registrado");
        }

        pasajero.setFechaHoraRegistro(LocalDateTime.now());
        Pasajero saved = pasajeroRepository.save(pasajero);  // 2. Guardar primero y asignar a 'saved'

        // --- Envío de correo de confirmación ---
        String subject = "Bienvenido a InterRide";
        String body = String.format(
                "Hola %s,%n%n¡Gracias por registrarte en InterRide! Ya puedes acceder a la plataforma y reservar viajes.%n%nSaludos,%nEl equipo de InterRide",
                saved.getNombre()
        );
        emailService.sendRegistrationConfirmation(saved.getCorreo(), subject, body);

        return saved;  // Devolver el objeto guardado
    }
}
