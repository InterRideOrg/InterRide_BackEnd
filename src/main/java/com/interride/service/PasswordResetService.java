package com.interride.service;

import com.interride.model.entity.PasswordResetToken;

public interface PasswordResetService {
    void createAndSendPasswordResetToken(String email) throws Exception;
    PasswordResetToken findByToken(String token);
    void removeResetToken(PasswordResetToken token);
    boolean isValidToken(String token);
    void resetPassword(String token, String newPassword);
}
