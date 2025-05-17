package com.interride.service;

import com.interride.dto.request.ConductorRegistroRequest;
import com.interride.dto.response.ConductorRegistroResponse;

public interface ConductorService {
    ConductorRegistroResponse registrarConductor(ConductorRegistroRequest request);
}
