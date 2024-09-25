package com.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.service.CustomUserDetailsService;

@Component
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Xử lý khi người dùng đăng nhập thành công
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        boolean isActive = userDetailsService.isActiveUser(username); // Kiểm tra trạng thái kích hoạt của người dùng

        if (isActive) {
            // Nếu người dùng đã kích hoạt, chuyển hướng về trang chủ
            response.sendRedirect(request.getContextPath());
        } else {
            // Nếu người dùng chưa kích hoạt, chuyển hướng về trang hồ sơ để kích hoạt
            response.sendRedirect("user/profile");
        }
    }
}
