package com.interride.service;

import com.interride.mapper.NotificacionMapper;
import com.interride.repository.ConductorRepository;
import com.interride.repository.NotificacionRepository;
import com.interride.repository.PasajeroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NotificacionServiceUnitTest {
    @Mock private NotificacionRepository notificacionRepository;
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private ConductorRepository conductorRepository;
    @Mock private NotificacionMapper notificacionMapper;

    @InjectMocks
    private NotificacionService notificacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS
}
