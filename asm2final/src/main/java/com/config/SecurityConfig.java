package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
    	
        UserDetails user1 = User.builder()
            .username("use1")
            .password("{noop}12345")
            .roles("USER")
            .build();

        UserDetails hr1 = User.builder()
                .username("hr1")
                .password("{noop}12345")
                .roles("HR")
                .build();

        UserDetails admin1 = User.builder()
                .username("admin1")
                .password("{noop}12345")
                .roles("ADMIN")
                .build();
        
        return new InMemoryUserDetailsManager(user1,hr1 , admin1);
        
    }
}

