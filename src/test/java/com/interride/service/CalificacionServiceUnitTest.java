package com.interride.service;

import com.interride.mapper.CalificacionMapper;
import com.interride.repository.CalificacionRepository;
import com.interride.repository.ConductorRepository;
import com.interride.repository.PasajeroRepository;
import com.interride.repository.ViajeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CalificacionServiceUnitTest {
    @Mock private CalificacionMapper calificacionMapper;
    @Mock private CalificacionRepository calificacionRepository;
    @Mock private ConductorRepository conductorRepository;
    @Mock private ViajeRepository viajeRepository;
    @Mock private PasajeroRepository pasajeroRepository;

    @InjectMocks
    private CalificacionService calificacionService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    //TESTS
}
