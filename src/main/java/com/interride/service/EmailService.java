package com.interride.service;

public interface EmailService {
    void sendRegistrationConfirmation(String to, String subject, String body);
}