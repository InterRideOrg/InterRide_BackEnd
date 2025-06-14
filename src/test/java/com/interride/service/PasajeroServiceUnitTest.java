package com.interride.service;

import com.interride.dto.response.PasajeroPerfilPublicoResponse;
import com.interride.exception.ResourceNotFoundException;
import com.interride.mapper.PasajeroMapper;
import com.interride.model.entity.Pasajero;
import com.interride.repository.PasajeroRepository;
import com.interride.service.Impl.PasajeroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PasajeroServiceUnitTest {
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private PasajeroMapper mapper;
    @Mock private PasswordEncoder encoder;
    @Mock private EmailService emailService;

    @InjectMocks
    private PasajeroServiceImpl pasajeroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS

    @Test
    @DisplayName("HU12 - CP01 - Obtencion de perfil público del pasajero correctamente")
    void obtenerPerfilPasajero_success_returnPublicProfile() {
        Integer idPasajero = 5;

        Pasajero pasajero = Pasajero.builder()
                .id(idPasajero)
                .nombre("Juan")
                .apellidos("Pérez")
                .correo("juan.perez@mail.com")
                .telefono("987654321")
                .username("juanpe")
                .build();

        PasajeroPerfilPublicoResponse expectedResponse = new PasajeroPerfilPublicoResponse(
                "Juan", "Pérez", "juan.perez@mail.com", "987654321", "juanpe"
        );

        when(pasajeroRepository.findById(idPasajero)).thenReturn(Optional.of(pasajero));
        when(mapper.toPublicProfileDTO(pasajero)).thenReturn(expectedResponse);

        PasajeroPerfilPublicoResponse resultado = pasajeroService.obtenerPerfilPasajero(idPasajero);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.nombres());
        assertEquals("Pérez", resultado.apellidos());
        assertEquals("juan.perez@mail.com", resultado.correo());
        assertEquals("987654321", resultado.telefono());
        assertEquals("juanpe", resultado.username());
    }

    @Test
    @DisplayName("HU12 - CP02 - Pasajero no encontrado ")
    void obtenerPerfilPasajero_pasajeroNoEncontrado_throwsException() {
        Integer idPasajero = 999;

        when(pasajeroRepository.findById(idPasajero)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> pasajeroService.obtenerPerfilPasajero(idPasajero));

        assertEquals("Pasajero con ID 999 no encontrado.", exception.getMessage());
    }

}
