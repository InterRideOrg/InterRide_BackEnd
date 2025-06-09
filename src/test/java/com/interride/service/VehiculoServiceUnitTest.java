package com.interride.service;

import com.interride.repository.ConductorRepository;
import com.interride.repository.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VehiculoServiceUnitTest {
    @Mock private VehiculoRepository vehiculoRepository;
    @Mock private ConductorRepository conductorRepository;

    @InjectMocks
    private VehiculoService vehiculoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS

}
