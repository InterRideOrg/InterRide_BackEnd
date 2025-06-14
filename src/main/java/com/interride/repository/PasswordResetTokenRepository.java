package com.interride.repository;

import java.util.Optional;

import com.interride.model.entity.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import com.interride.model.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);

}
