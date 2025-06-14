package com.interride.controller;

import com.interride.dto.request.PasajeroRegistrationRequest;
import com.interride.dto.response.UsuarioResponse;
import com.interride.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerRegistrarPasajeroUnitTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private AuthController controller;

    private PasajeroRegistrationRequest req;

    private UsuarioResponse expected;

    @BeforeEach
    void setUp() {
        req = new PasajeroRegistrationRequest();
        req.setNombre("Juan");
        req.setApellidos("Pérez");
        req.setCorreo("juan@mail.com");
        req.setTelefono("987654321");
        req.setPassword("plainPass");

        expected = mock(UsuarioResponse.class);
    }

    /* ---------- CP01 ---------- */
    @Test
    @DisplayName("CP01 – Registro exitoso")
    void givenValidData_whenRegister_thenReturnProfile() {
        when(usuarioService.registrarPasajero(eq(req))).thenReturn(expected);

        ResponseEntity<UsuarioResponse> res = controller.registrarPasajero(req);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(expected, res.getBody());
        verify(usuarioService).registrarPasajero(eq(req));
    }

    /* ---------- CP02 ---------- */
    @Test
    @DisplayName("CP02 – Email duplicado")
    void givenExistingEmail_whenRegister_thenThrow() {
        doThrow(new RuntimeException("email"))
                .when(usuarioService).registrarPasajero(eq(req));

        assertThrows(RuntimeException.class, () -> controller.registrarPasajero(req));
        verify(usuarioService).registrarPasajero(eq(req));
    }

    /* ---------- CP03 ---------- */
    @Test
    @DisplayName("CP03 – Teléfono duplicado")
    void givenExistingPhone_whenRegister_thenThrow() {
        req.setCorreo("nuevo@mail.com");
        doThrow(new RuntimeException("phone"))
                .when(usuarioService).registrarPasajero(eq(req));

        assertThrows(RuntimeException.class, () -> controller.registrarPasajero(req));
        verify(usuarioService).registrarPasajero(eq(req));
    }

    /* ---------- CP04 ---------- */
    @Test
    @DisplayName("CP04 – Error inesperado en capa de servicio se propaga")
    void givenServiceFailure_whenRegister_thenPropagate() {
        doThrow(new RuntimeException("DB down"))
                .when(usuarioService).registrarPasajero(eq(req));

        assertThrows(RuntimeException.class, () -> controller.registrarPasajero(req));
        verify(usuarioService).registrarPasajero(eq(req));
    }
}
