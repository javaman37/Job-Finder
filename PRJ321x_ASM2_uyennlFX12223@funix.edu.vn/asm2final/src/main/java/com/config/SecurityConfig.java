package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler;

    // Cung cấp dịch vụ xác thực người dùng tùy chỉnh
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    // Cung cấp mã hóa mật khẩu bằng BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình xác thực với dịch vụ UserDetails và mã hóa mật khẩu
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
            .passwordEncoder(passwordEncoder());
    }

    // Xử lý sự kiện sau khi đăng nhập thành công
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler();
    }

    // Cấu hình bảo mật HTTP, điều khiển quyền truy cập URL
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.csrf().disable() // Tắt bảo vệ CSRF cho mục đích phát triển
            .authorizeRequests()
            .antMatchers("/auth/**", "/assets/**", "/assets/public/**")
            .permitAll() // Cho phép truy cập không cần xác thực vào các URL này
            .antMatchers("/").hasAnyAuthority("ROLE_USER", "ROLE_HR") // Truy cập root với vai trò USER hoặc HR
            .antMatchers("/hr/**").hasAuthority("ROLE_HR") // Chỉ HR có thể truy cập vào /hr
            .antMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_HR") // Cả USER và HR đều truy cập được /user
            .anyRequest().authenticated() // Các yêu cầu khác cần xác thực
            .and()
            .formLogin()
            .loginPage("/auth/login") // Đặt trang đăng nhập tùy chỉnh
            .successHandler(customLoginSuccessHandler) // Xử lý sau khi đăng nhập thành công
            .usernameParameter("email") // Tham số tên người dùng là "email"
            .passwordParameter("password") // Tham số mật khẩu là "password"
            .loginProcessingUrl("/j_spring_security_login") // URL xử lý đăng nhập
            .and().addFilterBefore(filter, CsrfFilter.class)
            .logout()
            .logoutUrl("/auth/logout") // URL xử lý đăng xuất
            .logoutSuccessUrl("/") // Chuyển hướng sau khi đăng xuất thành công
            .permitAll();
    }
}
