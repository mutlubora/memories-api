package com.memories.api.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final TokenFilter tokenFilter;

    public SecurityConfig(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authentication) -> authentication
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/v1/users/{id}")).authenticated()
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/memories")).authenticated()
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/memory-attachments")).authenticated()
                    .anyRequest().permitAll()
        );

        http.httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(new AuthEntryPoint()));

        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }


}
