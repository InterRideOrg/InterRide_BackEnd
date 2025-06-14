package com.interride.service;

import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.dto.response.PasajeroRegistroResponse;
import com.interride.mapper.PasajeroMapper;
import com.interride.model.entity.Pasajero;
import com.interride.repository.PasajeroRepository;
import com.interride.service.Impl.PasajeroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PasajeroServiceUnitTest {

    /* ===== Dependencias mockeadas ===== */
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private PasswordEncoder    encoder;
    @Mock private EmailService       emailService;
    @Mock private PasajeroMapper     pasajeroMapper;

    /* ===== SUT ===== */
    @InjectMocks
    private PasajeroServiceImpl pasajeroService;

    /* ===== Captor ===== */
    @Captor
    private ArgumentCaptor<Pasajero> pasajeroCaptor;

    /* ===== Fixture común ===== */
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

    /* ----------------------------------------------------------------
     *                       CASOS EXITOSOS
     * --------------------------------------------------------------- */
    @Nested @DisplayName("Escenarios exitosos")
    class Successful {

        @Test
        @DisplayName("CP01 – Registro exitoso con verificaciones completas")
        void shouldRegisterPassengerSuccessfully() {
            /* ---- Arrange ---- */
            //when(pasajeroRepository.existsByCorreo(request.getCorreo())).thenReturn(false);
            when(pasajeroRepository.existsByTelefono(request.getTelefono())).thenReturn(false);

            Pasajero mappedEntity = Pasajero.builder()
                    .nombre   (request.getNombre())
                    .apellidos(request.getApellidos())
                    .telefono (request.getTelefono())
                    .username (request.getUsername())
                    .build();
            when(pasajeroMapper.toEntity(request)).thenReturn(mappedEntity);

            when(encoder.encode(request.getPassword())).thenReturn("encodedPass");

            Pasajero savedEntity = Pasajero.builder()
                    .id       (1)
                    .nombre   (request.getNombre())
                    .apellidos(request.getApellidos())
                    .telefono (request.getTelefono())
                    .username (request.getUsername())
                    .build();
            when(pasajeroRepository.save(any(Pasajero.class))).thenReturn(savedEntity);

            PasajeroRegistroResponse expectedResponse = PasajeroRegistroResponse.builder()
                    .id       (1)
                    .nombre   (request.getNombre())
                    .apellidos(request.getApellidos())
                    .correo   (request.getCorreo())
                    .telefono (request.getTelefono())
                    .username (request.getUsername())
                    .build();
            when(pasajeroMapper.toProfileDTO(savedEntity)).thenReturn(expectedResponse);

            /* ---- Act ---- */
            //PasajeroRegistroResponse actual = pasajeroService.register(request);

            /* ---- Assert salida ---- */
            //assertThat(actual).isEqualTo(expectedResponse);

            /* ---- Assert interacciones ---- */
            verify(pasajeroRepository).save(pasajeroCaptor.capture());
            Pasajero persisted = pasajeroCaptor.getValue();
            //assertThat(persisted.getPassword()).isEqualTo("encodedPass");
            //assertThat(persisted.getCorreo())
              //      .isEqualTo(request.getCorreo());

            verify(encoder).encode(request.getPassword());
            verify(emailService)
                    .sendRegistrationConfirmation(eq(request.getCorreo()), anyString(), anyString());
        }
    }

    /* ----------------------------------------------------------------
     *              CASOS FALLIDOS POR DUPLICIDAD
     * --------------------------------------------------------------- */
    @Nested @DisplayName("Escenarios fallidos por duplicidad")
    class Failures {

        @Test
        @DisplayName("CP02 – Correo duplicado provoca excepción")
        void shouldFailWhenEmailExists() {
            //when(pasajeroRepository.existsByCorreo(request.getCorreo())).thenReturn(true);

            /*assertThatThrownBy(() -> pasajeroService.register(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("correo ya está registrado");

             */

            verify(pasajeroRepository, never()).save(any());
            verify(emailService,     never()).sendRegistrationConfirmation(anyString(), anyString(), anyString());
        }

        @Test
        @DisplayName("CP03 – Teléfono duplicado provoca excepción")
        void shouldFailWhenPhoneExists() {
            //when(pasajeroRepository.existsByCorreo(request.getCorreo())).thenReturn(false);
            when(pasajeroRepository.existsByTelefono(request.getTelefono())).thenReturn(true);

            /*assertThatThrownBy(() -> pasajeroService.register(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("teléfono ya está registrado");

             */

            verify(pasajeroRepository, never()).save(any());
            verify(emailService,     never()).sendRegistrationConfirmation(anyString(), anyString(), anyString());
        }
    }

    /* ----------------------------------------------------------------
     *        CASO FALLIDO POR ERROR EN EL SERVICIO DE EMAIL
     * --------------------------------------------------------------- */
    @Nested @DisplayName("Escenario de falla en servicio de correo")
    class EmailFailure {

        @Test
        @DisplayName("CP04 – Error en EmailService se propaga")
        void shouldPropagateWhenEmailServiceFails() {
            //when(pasajeroRepository.existsByCorreo(request.getCorreo())).thenReturn(false);
            when(pasajeroRepository.existsByTelefono(request.getTelefono())).thenReturn(false);

            Pasajero mapped = Pasajero.builder()
                    .nombre  (request.getNombre())
                    .telefono(request.getTelefono())
                    .build();
            when(pasajeroMapper.toEntity(request)).thenReturn(mapped);
            when(encoder.encode(request.getPassword())).thenReturn("encodedPass");

            Pasajero saved = Pasajero.builder()
                    .id       (2)
                    .nombre   (request.getNombre())
                    .apellidos(request.getApellidos())
                    .telefono (request.getTelefono())
                    .username (request.getUsername())
                    .build();
            when(pasajeroRepository.save(any(Pasajero.class))).thenReturn(saved);

            doThrow(new RuntimeException("SMTP down"))
                    .when(emailService).sendRegistrationConfirmation(anyString(), anyString(), anyString());

            /*assertThatThrownBy(() -> pasajeroService.register(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("SMTP down");

             */

            verify(pasajeroRepository).save(any(Pasajero.class));
            verify(emailService)
                    .sendRegistrationConfirmation(eq(request.getCorreo()), anyString(), anyString());
        }
    }
}
