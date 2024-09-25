package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    // Gửi email đơn giản với địa chỉ nhận, tiêu đề và nội dung
    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(to);         // Đặt địa chỉ email nhận
        message.setSubject(subject); // Đặt tiêu đề email
        message.setText(text);       // Đặt nội dung email
        emailSender.send(message);   // Gửi email
    }
}
