package com.config;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(null);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
            .passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
	        .antMatchers("/auth/**","/assets/**","/assets/public/**")
	        .permitAll()
            .antMatchers("/").hasAnyAuthority("USER", "HR")
            .antMatchers("/hr/**").hasAuthority("HR")
            .antMatchers("/user/**").hasAuthority("USER")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/auth/login") // URL của trang đăng nhập
            .defaultSuccessUrl("/", true)// URL mặc định sau khi đăng nhập thành công
            .usernameParameter("email")
            .passwordParameter("password")
            .loginProcessingUrl("/j_spring_security_login")
            .and()
            .logout()
            .logoutUrl("/auth/logout") // URL xử lý logout
            .logoutSuccessUrl("/")
            .permitAll();
    }
}
