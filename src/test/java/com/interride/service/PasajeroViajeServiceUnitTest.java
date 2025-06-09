package com.interride.service;

import com.interride.mapper.PasajeroViajeMapper;
import com.interride.mapper.UbicacionMapper;
import com.interride.repository.NotificacionRepository;
import com.interride.repository.PasajeroViajeRepository;
import com.interride.repository.UbicacionRepository;
import com.interride.repository.ViajeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PasajeroViajeServiceUnitTest {
    @Mock private PasajeroViajeRepository pasajeroViajeRepository;
    @Mock private ViajeRepository viajeRepository;
    @Mock private UbicacionRepository ubicacionRepository;
    @Mock private NotificacionRepository notificacionRepository;
    @Mock private UbicacionMapper ubicacionMapper;
    @Mock private PasajeroViajeMapper pasajeroViajeMapper;

    @InjectMocks
    private PasajeroViajeService pasajeroViajeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS

}
