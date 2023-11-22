package com.fc.shimpyo_be.global.config;

import com.fc.shimpyo_be.global.config.jwt.JwtAccessDeniedHandler;
import com.fc.shimpyo_be.global.config.jwt.JwtAuthenticationEntryPoint;
import com.fc.shimpyo_be.global.config.jwt.JwtAuthenticationFilter;
import com.fc.shimpyo_be.global.config.jwt.JwtSecurityConfig;
import com.fc.shimpyo_be.global.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final CorsConfig corsConfig;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private static final String[] PERMIT_URL_ARRAY = {
        "/",
        "/error",
        "/docs/**",
        "/api/auth/**",
        "/api/products/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(PERMIT_URL_ARRAY)
                .permitAll()
                .anyRequest()
                .hasRole("USER"))
            .formLogin(AbstractHttpConfigurer::disable)
            .addFilter(corsConfig.corsFilter())
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(AuthenticationManager -> AuthenticationManager
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler))
            .apply(new JwtSecurityConfig(jwtTokenProvider));
        return http.build();
    }
}
