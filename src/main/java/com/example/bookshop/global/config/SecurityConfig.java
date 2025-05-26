package com.example.bookshop.global.config;


import com.example.bookshop.global.security.AuthenticationFilter;
import com.example.bookshop.global.security.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationFilter authenticationFilter;

    private final JwtExceptionFilter jwtExceptionFilter;



    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(CsrfConfigurer::disable) // 전체적으로 CSRF 비활성화
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/user/**","/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, AuthenticationFilter.class);
        return httpSecurity.build();
    }


}