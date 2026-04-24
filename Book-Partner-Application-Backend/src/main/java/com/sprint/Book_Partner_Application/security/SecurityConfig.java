package com.sprint.Book_Partner_Application.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails vennila = User.withUsername("vennila")
                .password(passwordEncoder.encode("vennila123"))
                .roles("STORE")
                .build();

        UserDetails akalya = User.withUsername("akalya")
                .password(passwordEncoder.encode("akalya123"))
                .roles("AUTHOR")
                .build();

        UserDetails sachitha = User.withUsername("sachitha")
                .password(passwordEncoder.encode("sachitha123"))
                .roles("BOOK")
                .build();

        UserDetails sanjai = User.withUsername("sanjai")
                .password(passwordEncoder.encode("sanjai123"))
                .roles("PUBLISHER", "EMPLOYEE")
                .build();

        UserDetails theja = User.withUsername("theja")
                .password(passwordEncoder.encode("theja123"))
                .roles("SALE")
                .build();

        return new InMemoryUserDetailsManager(vennila, akalya, sachitha, sanjai, theja);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/auth/me").authenticated()
                        .requestMatchers("/api/v1/stores/**", "/api/v1/discounts/**").hasRole("STORE")
                        .requestMatchers("/api/v1/authors/**").hasRole("AUTHOR")
                        .requestMatchers("/api/titles/**").hasRole("BOOK")
                        .requestMatchers("/api/publishers/**").hasRole("PUBLISHER")
                        .requestMatchers("/api/employees/**").hasRole("EMPLOYEE")
                        .requestMatchers("/api/v1/transactions/**").hasRole("SALE")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\":true,\"message\":\"Login successful\"}");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\":false,\"message\":\"Invalid username or password\"}");
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\":true,\"message\":\"Logout successful\"}");
                        })
                )
                .exceptionHandling(exception -> exception
                        .defaultAuthenticationEntryPointFor((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\":false,\"message\":\"Authentication required\"}");
                        }, new AntPathRequestMatcher("/api/**"))
                        .defaultAuthenticationEntryPointFor((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\":false,\"message\":\"Authentication required\"}");
                        }, new AntPathRequestMatcher("/auth/**"))
                        .defaultAccessDeniedHandlerFor((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\":false,\"message\":\"Access denied\"}");
                        }, new AntPathRequestMatcher("/api/**"))
                )
                .sessionManagement(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
