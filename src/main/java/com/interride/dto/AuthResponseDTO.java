package com.interride.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data @AllArgsConstructor
@Builder
public class AuthResponseDTO {

    private String token;
    private long   expiresIn;

    @Default
    private String tokenType = "Bearer";

    @Default
    private PasajeroProfileDTO profile = null;
}