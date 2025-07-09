package com.interride.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.interride.dto.request.GoogleLoginRequest;
import com.interride.dto.response.AuthResponse;
import com.interride.exception.BusinessRuleException;
import com.interride.exception.ResourceNotFoundException;
import com.interride.model.entity.Usuario;
import com.interride.security.TokenProvider;
import com.interride.security.google.GoogleTokenVerifierUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GoogleLoginService {
    AuthResponse loginWithGoogle(GoogleLoginRequest request);

}
