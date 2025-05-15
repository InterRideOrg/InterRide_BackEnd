package com.interride.service;

import java.time.Instant;

public interface TokenBlacklistService {
    void add(String token, Instant expiresAt);
    boolean contains(String token);
}