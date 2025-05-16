package com.interride.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pasajero_id", nullable = false)
    private Pasajero pasajero;

    private Instant expiryDate;

    protected PasswordResetToken() { }

    public PasswordResetToken(String token, Pasajero pasajero, Instant expiryDate) {
        this.token = token;
        this.pasajero = pasajero;
        this.expiryDate = expiryDate;
    }

    public String getToken() { return token; }
    public Pasajero getPasajero() { return pasajero; }
    public boolean isExpired() { return Instant.now().isAfter(expiryDate); }
}
