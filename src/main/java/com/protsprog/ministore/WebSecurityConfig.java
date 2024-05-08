package com.protsprog.ministore;

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

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

		User.UserBuilder users = User.builder();

		UserDetails admin = users
				.username("admin")
				.password(passwordEncoder.encode("admin"))
				// .disabled(true)
				// .authorities("db")
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
								"/error",
								"/assets/**",
								"/css/**",
								"/js/**",
								"/images/**",
								"/actuator/health")
						.permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				// .httpBasic(Customizer.withDefaults())
				.sessionManagement((session) -> session.maximumSessions(1))
				.formLogin((form) -> form
						.loginPage("/login")
						.permitAll())
				.logout((logout) -> logout
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true)
						.permitAll());

		return http.build();
	}
}
