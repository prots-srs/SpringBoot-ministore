package com.protsdev.ministore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Value("${user.admin.name}")
    private String adminName;

    @Value("${user.admin.password}")
    private String adminPassword;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        User.UserBuilder users = User.builder();

        UserDetails admin = users
                .username(adminName)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((requests) -> requests
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/",
                                "/content/**",
                                "/favicon.ico",
                                "/fonts/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/error",
                                "/localize",
                                "/h2-console/**",
                                "/actuator/health")
                        .permitAll()
                        .requestMatchers("/actuator/**", "/panel/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated())
                // .sessionManagement((session) -> session.maximumSessions(1))
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll())
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .csrf(Customizer.withDefaults())
                .securityMatcher("/h2-console/**", "/actuator/**", "/panel/**", "/login");

        return http.build();
    }
}
