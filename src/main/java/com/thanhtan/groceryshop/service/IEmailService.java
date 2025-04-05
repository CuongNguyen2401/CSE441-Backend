package com.thanhtan.groceryshop.service;

import jakarta.mail.MessagingException;

import java.util.Map;

public interface IEmailService {
    void sendTemplateEmail(String to, String subject, Map<String, Object> templateModel) throws MessagingException;
}