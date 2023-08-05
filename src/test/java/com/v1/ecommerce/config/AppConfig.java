package com.v1.ecommerce.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class AppConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->
                        auth.requestMatchers("/api/**").authenticated().anyRequest().permitAll()
                )
                .addFilterBefore((Filter) new JwtValidator(), BasicAuthenticationFilter.class).csrf(csrf -> csrf.disable())
                .cors(cors-> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfig = new CorsConfiguration();
                        corsConfig.setAllowedOrigins(Arrays.asList(
                                "http://localhost:3000/",
                                "http://localhost:8000/"
                        ));
                        corsConfig.setAllowedMethods(Collections.singletonList("*"));
                        corsConfig.setAllowCredentials(true);
                        corsConfig.setAllowedHeaders(Collections.singletonList("*"));
                        corsConfig.setExposedHeaders(Arrays.asList("Authorization"));
                        corsConfig.setMaxAge(3600L);
                        return corsConfig;
                    }
                }));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
