package com.interride.service.Impl;

import com.interride.service.TokenBlacklistService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryTokenBlacklistService implements TokenBlacklistService {
    private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

    @Override
    public void add(String token, Instant expiresAt) {
        blacklist.put(token, expiresAt);
    }

    @Override
    public boolean contains(String token) {
        Instant exp = blacklist.get(token);
        if (exp == null) return false;
        if (exp.isBefore(Instant.now())) {
            blacklist.remove(token);         // limpieza automática
            return false;
        }
        return true;
    }
}