package com.cuongnguyen.cse441.util;

import com.cuongnguyen.cse441.service.IEmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailSender {

    IEmailService emailService;

//    public void sendTestEmail() {
//        String recipient = "tan.ngo.cit20@eiu.edu.vn";
//        String subject = "Welcome to Our Service";
//
//        Map<String, Object> templateModel = new HashMap<>();
//        templateModel.put("name", "John Doe");
//        templateModel.put("activationLink", "http://example.com/activate?token=someToken");
//
//        try {
//            emailService.sendTemplateEmail(recipient, subject, templateModel);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
}
