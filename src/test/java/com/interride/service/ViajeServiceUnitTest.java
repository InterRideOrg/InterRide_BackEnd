package com.interride.service;

import com.interride.mapper.PasajeroViajeMapper;
import com.interride.mapper.UbicacionMapper;
import com.interride.mapper.ViajeMapper;
import com.interride.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ViajeServiceUnitTest {
    @Mock private ViajeRepository viajeRepository;
    @Mock private NotificacionRepository notificacionRepository;
    @Mock private ConductorRepository conductorRepository;
    @Mock private PasajeroViajeRepository pasajeroViajeRepository;
    @Mock private UbicacionRepository ubicacionRepository;
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private CalificacionRepository calificacionRepository;
    @Mock private ViajeMapper viajeMapper;
    @Mock private UbicacionMapper ubicacionMapper;
    @Mock private PasajeroViajeMapper pasajeroViajeMapper;

    @InjectMocks
    private ViajeService viajeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS
}
