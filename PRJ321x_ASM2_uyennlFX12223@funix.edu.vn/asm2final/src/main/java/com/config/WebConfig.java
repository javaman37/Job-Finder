package com.config;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com")
public class WebConfig implements WebMvcConfigurer {

    // Spring + Thymeleaf cần ApplicationContext để thiết lập Template Resolver
    @Autowired
    private ApplicationContext applicationContext;

    // Cấu hình quản lý tài nguyên tĩnh (CSS, JS, fonts, images, file uploads)
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/css/**").addResourceLocations("/assets/css/");
        registry.addResourceHandler("/assets/js/**").addResourceLocations("/assets/js/");
        registry.addResourceHandler("/assets/fonts/**").addResourceLocations("/assets/fonts/");
        registry.addResourceHandler("/assets/images/**").addResourceLocations("/assets/images/");
        registry.addResourceHandler("/assets/file-upload/**").addResourceLocations("/assets/file-upload/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
    }

    // Cấu hình Thymeleaf Template Resolver
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/WEB-INF/view/"); // Đường dẫn tới các file view (HTML)
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML); // Chế độ xử lý HTML
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding("UTF-8"); // Đảm bảo mã hóa UTF-8
        return templateResolver;
    }
    
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
    // Cấu hình Spring + Thymeleaf Template Engine
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true); // Hỗ trợ Spring EL expressions
        
        // Thêm Dialect để tích hợp Spring Security vào Thymeleaf
        templateEngine.setAdditionalDialects(new HashSet<IDialect>(Arrays.asList(securityDialect())));
        return templateEngine;
    }

    // Cấu hình Thymeleaf View Resolver để xử lý các view (HTML)
    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine()); // Sử dụng Template Engine đã cấu hình
        viewResolver.setCharacterEncoding("UTF-8"); // Đảm bảo mã hóa UTF-8 cho view
        return viewResolver;
    }

    // Cấu hình Spring Security Dialect để hỗ trợ các thẻ Thymeleaf liên quan đến bảo mật
    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

    // Cấu hình mã hóa mật khẩu với BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình CommonsMultipartResolver để hỗ trợ upload file
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8"); // Thiết lập mã hóa UTF-8 cho file upload
        resolver.setMaxUploadSize(50000000); // Giới hạn kích thước file upload là 50MB
        resolver.setMaxUploadSizePerFile(5000000); // Giới hạn kích thước file đơn là 5MB
        resolver.setMaxInMemorySize(5000000); // Giới hạn bộ nhớ sử dụng cho upload
        return resolver;
    }

}
