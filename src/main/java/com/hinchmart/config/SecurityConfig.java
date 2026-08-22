package com.hinchmart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(@Lazy JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
                config.setAllowedOrigins(java.util.List.of("http://localhost:5173", "http://localhost:5174", "http://localhost:5175"));
                config.setAllowedMethods(java.util.List.of("*"));
                config.setAllowedHeaders(java.util.List.of("*"));
                config.setAllowCredentials(true);
                return config;
            }))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
            )
            .authorizeHttpRequests(auth -> auth
                // Allow CORS Pre-flight Options requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Public Health Check Endpoint
                .requestMatchers("/health", "/health/**", "/api/health", "/api/health/**", "/api/v1/health", "/api/v1/health/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                // Public Auth Endpoints (v1 & default)
                .requestMatchers("/api/auth/**", "/api/v1/auth/**").permitAll()
                // Public Catalog Endpoints
                .requestMatchers(HttpMethod.GET, "/api/categories/**", "/api/v1/categories/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/subcategories/**", "/api/v1/subcategories/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/brands/**", "/api/v1/brands/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products/**", "/api/v1/products/**").permitAll()
                // Public Payment Gateway Config & Webhook Callbacks
                .requestMatchers(HttpMethod.GET, "/api/payments/config", "/api/v1/payments/config").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/payments/webhook", "/api/v1/payments/webhook").permitAll()
                // Swagger / OpenAPI UI Documentation
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // SUPER ADMIN ENDPOINTS
                .requestMatchers("/api/super-admin/**", "/api/v1/super-admin/**").hasRole("SUPER_ADMIN")
                // Admin Endpoints
                .requestMatchers("/api/admin/**", "/api/v1/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                // Seller Endpoints
                .requestMatchers("/api/seller/**", "/api/v1/seller/**").hasAnyRole("SELLER", "SELLER_ADMIN", "SELLER_STAFF", "ADMIN", "SUPER_ADMIN")
                // All other endpoints require authentication
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
