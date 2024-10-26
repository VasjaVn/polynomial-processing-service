package com.example.polynomial.config.security;

import com.example.polynomial.config.security.filter.JwtAuthenticationFilter;
import com.example.polynomial.util.validator.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenValidator jwtTokenValidator;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequest -> {
                    authorizeHttpRequest
                            .requestMatchers(
                                    "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**")
                            .permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenValidator);
    }
}
