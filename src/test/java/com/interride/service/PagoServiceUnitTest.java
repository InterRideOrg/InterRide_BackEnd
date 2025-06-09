package com.interride.service;

import com.interride.mapper.PagoMapper;
import com.interride.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PagoServiceUnitTest {
    @Mock private PagoMapper pagoMapper;
    @Mock private PagoRepository pagoRepository;
    @Mock private TarjetaRepository tarjetaRepository;
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private ConductorRepository conductorRepository;
    @Mock private NotificacionRepository notificacionRepository;

    @InjectMocks
    private PagoService pagoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS

}
