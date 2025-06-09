package com.interride.service;

import com.interride.mapper.PasajeroMapper;
import com.interride.repository.PasajeroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasajeroServiceUnitTest {
    @Mock private PasajeroRepository pasajeroRepository;
    @Mock private PasajeroMapper mapper;
    @Mock private PasswordEncoder encoder;
    @Mock private EmailService emailService;

    @InjectMocks
    private PasajeroService pasajeroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS
}
