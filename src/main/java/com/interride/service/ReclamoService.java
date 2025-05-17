package com.interride.service;

import com.interride.dto.request.ReclamoRequest;
import com.interride.dto.response.ReclamoResponse;

public interface ReclamoService {
    ReclamoResponse crearReclamo(ReclamoRequest request);
}
