package com.interride.service;

import com.interride.repository.ConductorRepository;
import com.interride.repository.ViajeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ConductorServiceUnitTest {
    @Mock private ConductorRepository conductorRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ViajeRepository viajeRepository;

    @InjectMocks
    private ConductorService conductorService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    //TESTS
}
