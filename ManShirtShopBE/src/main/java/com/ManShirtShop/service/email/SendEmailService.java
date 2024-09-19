package com.ManShirtShop.service.email;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender mailSender;

    // public String sendEmail() {

    // }

    // private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private volatile boolean isRunning = true;

    public void start(String to,String text) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject("Trạng Thái đơn hàng");
                message.setText(text);
                mailSender.send(message);
            } finally {
                executorService.shutdownNow();
            }
        });
    }
}
