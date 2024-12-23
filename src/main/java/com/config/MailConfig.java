package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    // Cấu hình JavaMailSender để gửi email qua SMTP
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        // Cấu hình SMTP server
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        // Cung cấp thông tin tài khoản email gửi
        mailSender.setUsername("uyennlfx12223@funix.edu.vn");
        mailSender.setPassword("kbru eall ovql tbzo"); // Mật khẩu ứng dụng hoặc mã xác thực

        // Cấu hình các thuộc tính bổ sung cho email
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp"); // Giao thức gửi email
        props.put("mail.smtp.auth", "true"); // Bật xác thực SMTP
        props.put("mail.smtp.starttls.enable", "true"); // Bật TLS cho bảo mật
        props.put("mail.debug", "true"); // Bật chế độ gỡ lỗi để xem thông tin gửi email
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Tin cậy với server SMTP
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Sử dụng giao thức TLS v1.2

        return mailSender;
    }
}
