package com.ManShirtShop.controller;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ManShirtShop.service.email.SendEmailService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "api/mail")
@Tag(name = "Mail api")
public class SendMailController {
    @Autowired
    SendEmailService sendEmail;

    @PostMapping(value = "send", consumes = { MediaType.APPLICATION_JSON_VALUE } )
    public ResponseEntity<?> getALl(@RequestBody String email) throws MessagingException {
        sendEmail.start(email,"Gửi r nha");
        return ResponseEntity.ok().body("Ô kê");
    }
}
