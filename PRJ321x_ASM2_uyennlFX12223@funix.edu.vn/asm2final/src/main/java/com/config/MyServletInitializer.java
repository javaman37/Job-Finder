package com.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // Cấu hình các dịch vụ và nguồn dữ liệu (root config)
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0]; // Không cấu hình cụ thể cho root, chỉ sử dụng servlet config
    }

    // Cấu hình controller, view resolver và handler mapping (servlet config)
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class }; // Chỉ định cấu hình lớp WebConfig
    }

    // Cấu hình các URL mapping cho DispatcherServlet
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" }; // Tất cả các yêu cầu sẽ được xử lý bởi DispatcherServlet
    }

    // Tùy chỉnh cấu hình để hỗ trợ tải file (multipart config)
    @Override
    protected void customizeRegistration(Dynamic registration) {
        // Thiết lập cấu hình tải file với kích thước tối đa 10MB
        registration.setMultipartConfig(new MultipartConfigElement("", 10000000, 10000000, 0));
    }
}
