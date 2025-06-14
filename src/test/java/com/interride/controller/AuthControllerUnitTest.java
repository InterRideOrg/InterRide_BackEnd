package com.interride.controller;

import com.interride.dto.request.LoginRequest;
import com.interride.dto.response.AuthResponse;
import com.interride.model.entity.Pasajero;
import com.interride.security.TokenProvider;
import com.interride.service.PasajeroService;
import com.interride.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

    @Mock private AuthenticationManager authManager;
    @Mock private PasajeroService pasajeroService;
    @Mock private ModelMapper modelMapper;
    @Mock private TokenProvider tokenProvider;

    @Mock private Authentication authentication;
    @Mock private UserPrincipal principal;

    @InjectMocks
    private AuthController authController;

    private LoginRequest request;

    @BeforeEach
    void setUp() {
        request = new LoginRequest();
        request.setCorreo("juan@mail.com");
        request.setPassword("plainPass");
    }

    @Test
    @DisplayName("CP01 – Credenciales válidas")
    void givenValidCredentials_whenLogin_thenReturnAuthResponse() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getId()).thenReturn(1);

        Pasajero pasajero = new Pasajero();
        when(pasajeroService.getById(1)).thenReturn(pasajero);

        PasajeroProfileResponse profile = new PasajeroProfileResponse();
        when(modelMapper.map(pasajero, PasajeroProfileResponse.class)).thenReturn(profile);

        when(tokenProvider.createAccessToken(authentication)).thenReturn("jwt");
        when(tokenProvider.getExpiration()).thenReturn(3600L);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("jwt", body.getToken());
        assertEquals(3600L, body.getExpiresIn());
        assertEquals(profile, body.getProfile());
    }

    @Test
    @DisplayName("CP02 – Contraseña incorrecta")
    void givenBadPassword_whenLogin_thenThrow() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad"));
        assertThrows(BadCredentialsException.class, () -> authController.login(request));
    }

    @Test
    @DisplayName("CP03 – Usuario no existe")
    void givenNonExistentUser_whenLogin_thenThrow() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("not found"));
        assertThrows(UsernameNotFoundException.class, () -> authController.login(request));
    }
}
