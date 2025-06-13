package com.interride.service;

import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.dto.response.PasajeroProfileResponse;
import com.interride.mapper.PasajeroMapper;
import com.interride.model.entity.Pasajero;
import com.interride.repository.PasajeroRepository;
import com.interride.service.Impl.PasajeroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasajeroServiceUnitTest {

    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private PasswordEncoder encoder;
    @Mock private EmailService emailService;
    @Mock private PasajeroMapper pasajeroMapper;

    @InjectMocks
    private PasajeroServiceImpl pasajeroService;

    private PasajeroRegistrationRequest request;

    @BeforeEach
    void setUp() {
        request = new PasajeroRegistrationRequest();
        request.setNombre("Juan");
        request.setApellidos("Pérez");
        request.setCorreo("juan@mail.com");
        request.setTelefono("987654321");
        request.setPassword("plainPass");
        request.setUsername("juanUser");
    }

    @Test
    @DisplayName("CP01 – Registro exitoso")
    void givenValidRequest_whenRegister_thenReturnProfileResponse() {
        when(pasajeroRepository.existsByCorreo(request.getCorreo())).thenReturn(false);
        when(pasajeroRepository.existsByTelefono(request.getTelefono())).thenReturn(false);

        Pasajero entityToSave = Pasajero.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .correo(request.getCorreo())
                .telefono(request.getTelefono())
                .username(request.getUsername())
                .build();
        when(pasajeroMapper.toEntity(any(PasajeroRegistrationRequest.class))).thenReturn(entityToSave);
        when(encoder.encode(request.getPassword())).thenReturn("encodedPass");

        Pasajero savedEntity = Pasajero.builder()
                .id(1)
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .correo(request.getCorreo())
                .telefono(request.getTelefono())
                .username(request.getUsername())
                .password("encodedPass")
                .build();
        when(pasajeroRepository.save(any(Pasajero.class))).thenReturn(savedEntity);

        PasajeroProfileResponse expected = PasajeroProfileResponse.builder()
                .id(1)
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .correo(request.getCorreo())
                .telefono(request.getTelefono())
                .username(request.getUsername())
                .build();
        when(pasajeroMapper.toProfileDTO(savedEntity)).thenReturn(expected);

        PasajeroProfileResponse actual = pasajeroService.register(request);

        assertEquals(expected, actual);
        verify(pasajeroRepository).save(any(Pasajero.class));
        verify(emailService).sendRegistrationConfirmation(eq(request.getCorreo()), anyString(), anyString());
    }

    @Test
    @DisplayName("CP02 – Correo duplicado")
    void givenExistingEmail_whenRegister_thenThrowException() {
        when(pasajeroRepository.existsByCorreo(request.getCorreo())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> pasajeroService.register(request));
        verify(pasajeroRepository, never()).save(any());
    }

    @Test
    @DisplayName("CP03 – Teléfono duplicado")
    void givenExistingPhone_whenRegister_thenThrowException() {
        when(pasajeroRepository.existsByCorreo(request.getCorreo())).thenReturn(false);
        when(pasajeroRepository.existsByTelefono(request.getTelefono())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> pasajeroService.register(request));
        verify(pasajeroRepository, never()).save(any());
    }

    @Test
    @DisplayName("CP04 – Falla en EmailService")
    void givenEmailServiceFailure_whenRegister_thenPropagateException() {
        when(pasajeroRepository.existsByCorreo(request.getCorreo())).thenReturn(false);
        when(pasajeroRepository.existsByTelefono(request.getTelefono())).thenReturn(false);

        Pasajero entity = Pasajero.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .telefono(request.getTelefono())
                .build();
        when(pasajeroMapper.toEntity(any(PasajeroRegistrationRequest.class))).thenReturn(entity);
        when(encoder.encode(request.getPassword())).thenReturn("encodedPass");
        when(pasajeroRepository.save(any(Pasajero.class))).thenReturn(entity);

        doThrow(new RuntimeException("SMTP down"))
                .when(emailService).sendRegistrationConfirmation(anyString(), anyString(), anyString());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> pasajeroService.register(request));
        assertTrue(ex.getMessage() != null && ex.getMessage().contains("SMTP"));

        verify(pasajeroRepository).save(any(Pasajero.class));
    }
}
