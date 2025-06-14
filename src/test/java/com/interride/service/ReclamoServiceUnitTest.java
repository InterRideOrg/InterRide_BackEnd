package com.interride.service;

import com.interride.repository.ConductorRepository;
import com.interride.repository.PasajeroRepository;
import com.interride.repository.ReclamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReclamoServiceUnitTest {
    @Mock private ReclamoRepository reclamoRepository;
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private ConductorRepository conductorRepository;

    @InjectMocks
    private ReclamoService reclamoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS
}
