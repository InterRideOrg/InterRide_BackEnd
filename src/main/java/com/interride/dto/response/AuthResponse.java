package com.interride.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data @AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private long   expiresIn;

    @Default
    private String tokenType = "Bearer";

    @Default
    private PasajeroProfileResponse profile = null;
}