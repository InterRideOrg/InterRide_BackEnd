package com.interride.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
public class AuthResponse {
    private String token;
    private String username;
    private String role;
}