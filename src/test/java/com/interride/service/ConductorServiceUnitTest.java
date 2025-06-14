package com.interride.service;

import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.response.ConductorRegistroResponse;
import com.interride.exception.DuplicateResourceException;
import com.interride.mapper.ConductorMapper;

import com.interride.model.entity.Conductor;
import com.interride.repository.ConductorRepository;
import com.interride.service.Impl.ConductorServiceImpl;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class ConductorServiceUnitTest {
    @Mock private ConductorRepository conductorRepository;
    @Mock private ConductorMapper conductorMapper;

    @InjectMocks
    private ConductorServiceImpl conductorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS
    @Test
    @DisplayName("UH04 - CP01 - Registro como conductor exitoso")
    void registrarConductor_success_returnMessageSuccesfully() {
        Conductor conductor = Conductor.builder().id(1).nombre("Juan").build();
        ConductorRegistroRequest request = new ConductorRegistroRequest(
                "Juan",
                "Pérez",
                "juan@example.com",
                "987654321",
                "juanito123",
                "securePass"
        );

        when(conductorRepository.existsByCorreo(request.correo())).thenReturn(false);
        when(conductorRepository.existsByTelefono(request.telefono())).thenReturn(false);
        when(conductorRepository.existsByUsername(request.username())).thenReturn(false);
        when(conductorMapper.toEntity(request)).thenReturn(conductor);
        when(conductorRepository.save(conductor)).thenReturn(conductor);

        ConductorRegistroResponse response = conductorService.registrarConductor(request);

        assertNotNull(response);
        assertEquals("Registro exitoso. Se le ha enviado un correo de confirmación.", response.mensaje());
    }

    @Test
    @DisplayName("UH04 - CP02 - Registro como conductor fallido por correo duplicado")
    void registrarConductor_correoDuplicated_throwsException() {
        ConductorRegistroRequest request = new ConductorRegistroRequest(
                "Juan",
                "Pérez",
                "juan@example.com",
                "987654321",
                "juanito123",
                "securePass"
        );
        when(conductorRepository.existsByCorreo(request.correo())).thenReturn(true);

        DuplicateResourceException ex = assertThrows(
                DuplicateResourceException.class,
                () -> conductorService.registrarConductor(request)
        );

        assertEquals("Correo ya registrado. Ingrese otro.", ex.getMessage());
    }

    @Test
    @DisplayName("UH04 - CP03 - Registro como conductor fallido por telefono duplicado")
    void registrarConductor_telefonoDuplicated_throwsException() {
        ConductorRegistroRequest request = new ConductorRegistroRequest(
                "Juan",
                "Pérez",
                "juan@example.com",
                "987654321",
                "juanito123",
                "securePass"
        );
        when(conductorRepository.existsByCorreo(request.correo())).thenReturn(false);
        when(conductorRepository.existsByTelefono(request.telefono())).thenReturn(true);

        DuplicateResourceException ex = assertThrows(
                DuplicateResourceException.class,
                () -> conductorService.registrarConductor(request)
        );

        assertEquals("Teléfono ya registrado. Ingrese otro.", ex.getMessage());
    }

    @Test
    @DisplayName("UH04 - CP04 - Registro como conductor fallido por username duplicado")
    void registrarConductor_usernameDuplicated_throwsException() {
        ConductorRegistroRequest request = new ConductorRegistroRequest(
                "Juan",
                "Pérez",
                "juan@example.com",
                "987654321",
                "juanito123",
                "securePass"
        );
        when(conductorRepository.existsByCorreo(request.correo())).thenReturn(false);
        when(conductorRepository.existsByTelefono(request.telefono())).thenReturn(false);
        when(conductorRepository.existsByUsername(request.username())).thenReturn(true);

        DuplicateResourceException ex = assertThrows(
                DuplicateResourceException.class,
                () -> conductorService.registrarConductor(request)
        );

        assertEquals("Username ya registrado. Ingrese otro.", ex.getMessage());
    }
}
