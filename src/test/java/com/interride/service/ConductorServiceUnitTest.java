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

}
